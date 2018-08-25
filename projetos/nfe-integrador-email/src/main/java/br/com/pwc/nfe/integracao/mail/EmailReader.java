package br.com.pwc.nfe.integracao.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import br.com.pwc.enums.CodigoRetornoEnum;
import br.com.pwc.nfe.integracao.mail.config.Account;
import br.com.pwc.nfe.integracao.mail.config.Autenticador;
import br.com.pwc.nfe.integracao.mail.config.ConfigEnum;
import br.com.pwc.nfe.integracao.mail.config.ConfiguraConta;
import br.com.pwc.nfe.integracao.mail.config.IntegradorEmailMessageFactory;
import br.com.pwc.nfe.integracao.mail.util.FileManager;
import br.com.pwc.nfe.integracao.mail.util.IntegradorException;
import br.com.pwc.nfe.integracao.mail.util.XmlInspector;

public class EmailReader implements Runnable{
	
	private static Logger LOGGER = Logger.getLogger(EmailReader.class);
	
	private IntegradorEmailMessageFactory messageFactory;
	
	private XmlInspector inspector;
	
	private Integrador integrador;
	
	private FileManager fileManager;
	
	private Properties configProperties;
	
	private Account conta;
	
	private Properties propriedadesDaConta;
	
	private int totalDeMensagens;
	
	public EmailReader(Account conta, IntegradorEmailMessageFactory messageFactory, XmlInspector xmlInspector, 
			Integrador integrador, FileManager fileManager, Properties configProperties) {
		this.conta = conta;
		this.messageFactory = messageFactory;
		this.inspector = xmlInspector;
		this.integrador = integrador;
		this.fileManager = fileManager;
		this.configProperties = configProperties;
		propriedadesDaConta = ConfiguraConta.getConfig(conta);
	}
	
	@Override
	public synchronized void run() {
		
		LOGGER.info(messageFactory.getMessage(MSG_INICIO_CONEXAO));

		Session sessao = Session.getInstance(propriedadesDaConta, getAutenticador(conta));
		LOGGER.info(messageFactory.getMessageWithParameter(MSG_CONTA_EM_PROCESSO_CONEXAO, conta.getUser(),
				conta.getProtocolo(), conta.getPort()));
		try {
			Store store = sessao.getStore();
			store.connect(conta.getServer(), conta.getUser(), conta.getPasswd());
			LOGGER.info(messageFactory.getMessage(MSG_SUCESSO_CONEXAO));
			readInboxAndSend(store);
			store.close();
		} catch (MessagingException e) {
			LOGGER.error(messageFactory.getMessage(MSG_ERRO_COMUNICACAO_MENSAGERIA), e);
		} 
		
	}

	private void readInboxAndSend(Store store) throws MessagingException {
		
		Folder folder = store.getFolder(INBOX_POP_NAME);
		folder.open(Folder.READ_WRITE);
		
		totalDeMensagens = folder.getMessageCount();
		
		// se tiver mensagem na caixa envia para o portal
		if(totalDeMensagens > 0) {
			
			LOGGER.info(messageFactory.getMessage(MSG_INICIANDO_INTEGRACAO));
			Message[] arrayDeMensagens = folder.getMessages();
			
			for(Message mensagem: arrayDeMensagens) {
				
				LOGGER.info(messageFactory.getMessage(MSG_PROCESS_EXTRACTING_FILE_ATTACHED));
				
				if(!folder.isOpen()) {
					folder.open(Folder.READ_WRITE);
				}
				
				try {
					
					if (mensagem.getContent() instanceof Multipart) {
						
						Multipart mult = (Multipart) mensagem.getContent();
							
						for (int i = 0; i < mult.getCount(); i++) {
								
							Part part = mult.getBodyPart(i);
							String nameOfFile = part.getFileName();
							String tipoDeConteudo = part.getDisposition();
							
							if (tipoDeConteudo != null && (tipoDeConteudo.equals(Part.ATTACHMENT) || tipoDeConteudo.equals(Part.INLINE))) {
								
								if (nameOfFile.endsWith(".xml")) {
									
									String xml = getAnexoAsString(part.getInputStream());
									LOGGER.info(messageFactory.getMessage(MSG_SUCESSO_PROCESS_EXTRACTING_FILE_ATTACHED));
											
									LOGGER.info(messageFactory.getMessageWithParameter(MSG_ENVIANDO_XML, nameOfFile));
									String retorno = integrador.enviaSolicitacao(xml);
									
									LOGGER.info(messageFactory.getMessage(MSG_OBTENDO_CODIGO_RETORNO));
									String codigoRetorno = inspector.identificaCodigoDeRetorno(retorno);
									
									String descricaoRetorno = inspector.identificaDescricaoDoRetorno(retorno);
										
									// Se o retorno e o código de retorno não são vazios faz o tratamento da resposta
									if (!StringUtils.isBlank(retorno)&& !StringUtils.isBlank(codigoRetorno)) {
										
										CodigoRetornoEnum codigoRetornoEnum = CodigoRetornoEnum.lookupByCodigo(codigoRetorno);
									
										if (codigoRetornoEnum != null) {
											
											// Se o codigo de retorno for 500 foi importado com sucesso
											if (CODIGO_SUCESSO_IMPORTACAO.equals(codigoRetornoEnum.getCodigo())) {
												
												File destino = fileManager.file(configProperties
														.getProperty(ConfigEnum.INTEGRACAO_DIR_IMPORTADOS.getKey()),nameOfFile);
												
												LOGGER.info(descricaoRetorno);
												
												LOGGER.info(messageFactory.getMessage(MSG_MOVENDO_PARA_SUCESSO)+ nameOfFile);
												fileManager.writeFile(destino, xml);
												
												// Cria o arquivo de retorno do portal na pasta de retorno
												File arquivoDeRetorno = fileManager.file(configProperties
														.getProperty(ConfigEnum.INTEGRACAO_DIR_RETORNOS.getKey()),nameOfFile);
												
												LOGGER.info(messageFactory.getMessage(MSG_CRIANDO_ARQUIVO_RETORNO));
												fileManager.writeFile(arquivoDeRetorno, retorno);
												
											} else {
												
												File destino = fileManager.file(configProperties.getProperty(
														ConfigEnum.INTEGRACAO_DIR_ERROS.getKey()) + "/" + codigoRetornoEnum, nameOfFile);
												
												LOGGER.info(descricaoRetorno);
												
												LOGGER.info(messageFactory.getMessageWithParameter(MSG_MOVENDO_PARA_ERRO, 
														codigoRetornoEnum) + " " + nameOfFile);
												fileManager.writeFile(destino, xml);
												
												// Cria o arquivo de retorno do portal na pasta de retorno
												File arquivoDeRetorno = fileManager.file(configProperties
														.getProperty(ConfigEnum.INTEGRACAO_DIR_RETORNOS.getKey()), nameOfFile);
												
												LOGGER.info(messageFactory.getMessage(MSG_CRIANDO_ARQUIVO_RETORNO));
												fileManager.writeFile(arquivoDeRetorno, retorno);
											}
											
										}
									} 
								}
							}
						}
					}
				// erro de comunicação com o portal
				} catch (IntegradorException e) {
					LOGGER.error(messageFactory.getMessage(MSG_ERRO_COMUNICACAO_MENSAGERIA),e);
				// erro ao extrair anexo
				} catch (IOException e) {
					LOGGER.error(messageFactory.getMessage(MSG_ERROR_PROCESS_EXTRACTING_FILE_ATTACHED), e);
					mensagem.setFlag(Flags.Flag.DELETED,true);
					if(folder.isOpen()) {
						folder.close(true);
					}
				}	
				
				// seta a mensagem para ser deletada
				LOGGER.info(messageFactory.getMessage(MSG_PROCESS_DELETE_MAIL_OF_INBOX));
				
				mensagem.setFlag(Flags.Flag.DELETED,true);
				if(folder.isOpen()) {
					folder.close(true);
				}
			}
			
		} else {
			LOGGER.info(messageFactory.getMessageWithParameter(MSG_NAO_EXISTEM_MENSAGENS, conta.getUser()));
		}
	}
										
	/**
	 * Método responsável por extrair conteudo do anexo.
	 * 
	 * @param in
	 *            Input do canal de bytes do anexo.
	 * @return {@link StringBuffer} StringBuffer com o anexo.
	 */
	private String getAnexoAsString(InputStream in) {
		StringBuffer xml = new StringBuffer();
		try {
			String line;
			BufferedReader bf = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			while ((line = bf.readLine()) != null) {
				xml.append(line).append("\n");
			}
		} catch (IOException e) {
			LOGGER.error(
					messageFactory.getMessage(MSG_ERROR_WRITE_IN_DISK_ON_FILE),
					e);
		}
		return xml.toString();
	}
	
	/**
	 * Método de autenticação de conta.
	 * @param account - Conta a ser autenticada
	 * @return {@link Autenticador}
	 */
	private Autenticador getAutenticador(Account account) {
		Autenticador autenticador = null;
		if(account.isSsl()) {
			autenticador = new Autenticador(account.getUser(), account.getPasswd());
		}
		return autenticador;
	}
	
	private static final String INBOX_POP_NAME = "INBOX";
	private static final String MSG_PROCESS_EXTRACTING_FILE_ATTACHED = "msgExtraindoAnexos";
	private static final String MSG_INICIO_CONEXAO = "msgIniciandoConexao";
	private static final String MSG_CONTA_EM_PROCESSO_CONEXAO = "msgObtendoDadosDaConta";
	private static final String MSG_SUCESSO_CONEXAO = "msgConectadoSucesso";
	private static final String MSG_ERRO_COMUNICACAO_MENSAGERIA = "msgErroComunicacao";
	private static final String MSG_INICIANDO_INTEGRACAO = "msgIniciandoIntegracao";
	private static final String MSG_NAO_EXISTEM_MENSAGENS = "msgNaoExistemMensagens";
	private static final String MSG_ERROR_WRITE_IN_DISK_ON_FILE = "msgErroAoGravarArquivo";
	private static final String MSG_PROCESS_DELETE_MAIL_OF_INBOX = "msgDeletandoEmail";
	private static final String MSG_SUCESSO_PROCESS_EXTRACTING_FILE_ATTACHED = "msgAnexosExtraidos";
	private final static String CODIGO_SUCESSO_IMPORTACAO = "500";
	private static final String MSG_OBTENDO_CODIGO_RETORNO = "msgObtendoCodigoRetorno";
	private static final String MSG_MOVENDO_PARA_SUCESSO = "msgMovendoXmlParaPastaImportados";
	private static final String MSG_MOVENDO_PARA_ERRO = "msgMovendoXmlParaErro";
	private static final String MSG_CRIANDO_ARQUIVO_RETORNO = "msgCriandoArquivoRetorno";
	private static final String MSG_ERROR_PROCESS_EXTRACTING_FILE_ATTACHED = "msgErroAoExtrairAnexos";
	private static final String MSG_ENVIANDO_XML = "msgEnviandoXml";

}
