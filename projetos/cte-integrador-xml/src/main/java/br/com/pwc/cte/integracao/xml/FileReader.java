package br.com.pwc.cte.integracao.xml;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import br.com.pwc.cte.integracao.xml.config.CodigoRetorno;
import br.com.pwc.cte.integracao.xml.config.ConfigEnum;
import br.com.pwc.cte.integracao.xml.config.IntegradorXmlMessageFactory;
import br.com.pwc.cte.integracao.xml.util.FileManager;
import br.com.pwc.cte.integracao.xml.util.XmlInspector;

public class FileReader implements Runnable{
	
	private static Logger LOGGER = Logger.getLogger(FileReader.class);
	
	private Integrador integrador;

	private XmlInspector inspector;

	private IntegradorXmlMessageFactory messageFactory;

	private Properties configProperties;
	
	private FileManager fileManager;
	
	private ConcurrentHashMap<String, File> mapaDeArquivos;
	
	
	public FileReader(IntegradorXmlMessageFactory messageFactory, XmlInspector inspector,
			Integrador integrador, FileManager fileManager,
			Properties configProperties) {
		
		this.messageFactory = messageFactory;
		this.inspector = inspector;
		this.integrador = integrador;
		this.fileManager = fileManager;
		this.configProperties = configProperties;
	}
	
	public void run() {
		
		File file = fileManager.getXmlOfNFeToSend();
		
		if(file != null) {
			
			try {
				
				// Faz uma solicitação de importação para o portal
				String retorno = integrador.enviaSolicitacao(FileUtils.readFileToString(file, "UTF-8"));
				
				LOGGER.info(messageFactory.getMessage(MSG_OBTENDO_CODIGO_RETORNO));
				String codigoRetorno = inspector.identificaCodigoDeRetorno(retorno);
				
				String descricaoRetorno = inspector.identificaDescricaoDoRetorno(retorno);
				
				// Se o retorno for nulo aconteceu um erro de comunicação
				if (StringUtils.isBlank(retorno)|| StringUtils.isBlank(codigoRetorno)) {
					
					File destino = fileManager.file(configProperties.getProperty(
							ConfigEnum.INTEGRACAO_DIR_ERROS_COMUNICACAO.getKey()), file.getName());
					
					LOGGER.info(messageFactory.getMessage(MSG_MOVENDO_PARA_ERRO_COMUNICACAO) + " " + file.getName());
					fileManager.moveFile(file, destino);
					
				} else {
					
					CodigoRetorno codigoRetornoEnum = CodigoRetorno.lookupByCodigo(codigoRetorno);
					
					// Se não houver codigo de retorno correspondente no enum houve erro de comunicação
					if(codigoRetornoEnum == null) {
						
						File destino = fileManager.file(configProperties.getProperty(
								ConfigEnum.INTEGRACAO_DIR_ERROS_COMUNICACAO.getKey()), file.getName());
						
						LOGGER.info(messageFactory.getMessage(MSG_MOVENDO_PARA_ERRO_COMUNICACAO) + " " + file.getName());
						fileManager.moveFile(file, destino);
						
					
					} else {
						
						// Se o codigo de retorno for 500 foi importado com sucesso
						if(CODIGO_SUCESSO_IMPORTACAO.equals(codigoRetornoEnum.getCodigo())) {
							
							File destino = fileManager.file(configProperties.getProperty(
									ConfigEnum.INTEGRACAO_DIR_IMPORTADOS.getKey()), file.getName());
							
							LOGGER.info(descricaoRetorno);
							
							LOGGER.info(messageFactory.getMessage(MSG_MOVENDO_PARA_SUCESSO) + " " + file.getName());
							fileManager.moveFile(file, destino);
							
							// Cria o arquivo de retorno do portal na pasta de retorno
							File arquivoDeRetorno = fileManager.file(configProperties.getProperty(
									ConfigEnum.INTEGRACAO_DIR_RETORNOS.getKey()), file.getName());
							
							LOGGER.info(messageFactory.getMessage(MSG_CRIANDO_ARQUIVO_RETORNO));
							fileManager.writeFile(arquivoDeRetorno, retorno);
							
						} else {
							
							File destino = fileManager.file(configProperties.getProperty(
									ConfigEnum.INTEGRACAO_DIR_ERROS.getKey()) + "/" + codigoRetornoEnum, file.getName());
							
							LOGGER.info(descricaoRetorno);
							
							LOGGER.info(messageFactory.getMessageWithParameter(MSG_MOVENDO_PARA_ERRO, codigoRetornoEnum) +
									" " + file.getName());
							fileManager.moveFile(file, destino);
							
							// Cria o arquivo de retorno do portal na pasta de retorno
							File arquivoDeRetorno = fileManager.file(configProperties.getProperty(
									ConfigEnum.INTEGRACAO_DIR_RETORNOS.getKey()), file.getName());
							
							LOGGER.info(messageFactory.getMessage(MSG_CRIANDO_ARQUIVO_RETORNO));
							fileManager.writeFile(arquivoDeRetorno, retorno);
						}
						
						// remove o item que foi manipulado do mapa e da pasta de envio
						fileManager.removeFile(file);
					}
				}
				
			} catch (Exception e) {
				LOGGER.error(messageFactory.getMessage(MSG_ERRO_COMUNICACAO_MENSAGERIA), e);
				
				if(file != null) {
					File destino = fileManager.file(configProperties.getProperty(
							ConfigEnum.INTEGRACAO_DIR_ERROS_COMUNICACAO.getKey()), file.getName());
					
					LOGGER.info(messageFactory.getMessage(MSG_MOVENDO_PARA_ERRO_COMUNICACAO));
					fileManager.moveFile(file, destino);
				}
			}
			
		} else {
			
			LOGGER.info(messageFactory.getMessage(MSG_NAO_EXISTEM_MENSAGENS));
		}
		
	}
	
	public void runOld() {
		
		
		for(String fileName: mapaDeArquivos.keySet()) {
			
			try {
				
				// Faz uma solicitação de importação para o portal
				String retorno = integrador.enviaSolicitacao(FileUtils.readFileToString(mapaDeArquivos.get(fileName), "UTF-8"));
				
				LOGGER.info(messageFactory.getMessage(MSG_OBTENDO_CODIGO_RETORNO));
				String codigoRetorno = inspector.identificaCodigoDeRetorno(retorno);
				
				String descricaoRetorno = inspector.identificaDescricaoDoRetorno(retorno);
				
				// Se o retorno for nulo aconteceu um erro de comunicação
				if (StringUtils.isBlank(retorno)|| StringUtils.isBlank(codigoRetorno)) {
					
					File destino = fileManager.file(configProperties.getProperty(
							ConfigEnum.INTEGRACAO_DIR_ERROS_COMUNICACAO.getKey()), mapaDeArquivos.get(fileName).getName());
					
					LOGGER.info(messageFactory.getMessage(MSG_MOVENDO_PARA_ERRO_COMUNICACAO) + " " + fileName);
					fileManager.moveFile(mapaDeArquivos.get(fileName), destino);
					continue;
				}
				
				CodigoRetorno codigoRetornoEnum = CodigoRetorno.lookupByCodigo(codigoRetorno);
				
				// Se não houver codigo de retorno correspondente no enum houve erro de comunicação
				if(codigoRetornoEnum == null) {
					
					File destino = fileManager.file(configProperties.getProperty(
							ConfigEnum.INTEGRACAO_DIR_ERROS_COMUNICACAO.getKey()), mapaDeArquivos.get(fileName).getName());
					
					LOGGER.info(messageFactory.getMessage(MSG_MOVENDO_PARA_ERRO_COMUNICACAO) + " " + fileName);
					fileManager.moveFile(mapaDeArquivos.get(fileName), destino);
					continue;
				}
				
				// Se o codigo de retorno for 500 foi importado com sucesso
				if(CODIGO_SUCESSO_IMPORTACAO.equals(codigoRetornoEnum.getCodigo())) {
					
					File destino = fileManager.file(configProperties.getProperty(
							ConfigEnum.INTEGRACAO_DIR_IMPORTADOS.getKey()), mapaDeArquivos.get(fileName).getName());
					
					LOGGER.info(descricaoRetorno);
					
					LOGGER.info(messageFactory.getMessage(MSG_MOVENDO_PARA_SUCESSO) + " " + fileName);
					fileManager.moveFile(mapaDeArquivos.get(fileName), destino);
					
					// Cria o arquivo de retorno do portal na pasta de retorno
					File arquivoDeRetorno = fileManager.file(configProperties.getProperty(
							ConfigEnum.INTEGRACAO_DIR_RETORNOS.getKey()), mapaDeArquivos.get(fileName).getName());
					
					LOGGER.info(messageFactory.getMessage(MSG_CRIANDO_ARQUIVO_RETORNO));
					fileManager.writeFile(arquivoDeRetorno, retorno);
					
				} else {
					
					File destino = fileManager.file(configProperties.getProperty(
							ConfigEnum.INTEGRACAO_DIR_ERROS.getKey()) + "/" + codigoRetornoEnum, mapaDeArquivos.get(fileName).getName());
					
					LOGGER.info(descricaoRetorno);
					
					LOGGER.info(messageFactory.getMessageWithParameter(MSG_MOVENDO_PARA_ERRO, codigoRetornoEnum) +
							" " + fileName);
					fileManager.moveFile(mapaDeArquivos.get(fileName), destino);
					
					// Cria o arquivo de retorno do portal na pasta de retorno
					File arquivoDeRetorno = fileManager.file(configProperties.getProperty(
							ConfigEnum.INTEGRACAO_DIR_RETORNOS.getKey()), mapaDeArquivos.get(fileName).getName());
					
					LOGGER.info(messageFactory.getMessage(MSG_CRIANDO_ARQUIVO_RETORNO));
					fileManager.writeFile(arquivoDeRetorno, retorno);
				}
				
				// remove o item que foi manipulado do mapa e da pasta de envio
				fileManager.removeFile(mapaDeArquivos.get(fileName));
				mapaDeArquivos.remove(fileName);
				
			} catch (Exception e) {
				LOGGER.error(messageFactory.getMessage(MSG_ERRO_COMUNICACAO_MENSAGERIA), e);
				
				if(mapaDeArquivos.get(fileName) != null) {
					File destino = fileManager.file(configProperties.getProperty(
							ConfigEnum.INTEGRACAO_DIR_ERROS_COMUNICACAO.getKey()), mapaDeArquivos.get(fileName).getName());
					
					LOGGER.info(messageFactory.getMessage(MSG_MOVENDO_PARA_ERRO_COMUNICACAO));
					fileManager.moveFile(mapaDeArquivos.get(fileName), destino);
				}
			}
		}
		
	}
	
	private final static String CODIGO_SUCESSO_IMPORTACAO = "500";
	private static final String MSG_ERRO_COMUNICACAO_MENSAGERIA = "msgErroComunicacao";
	private static final String MSG_OBTENDO_CODIGO_RETORNO = "msgObtendoCodigoRetorno";
	private static final String MSG_MOVENDO_PARA_ERRO_COMUNICACAO = "msgMovendoXmlParaErroComunicacao";
	private static final String MSG_MOVENDO_PARA_SUCESSO = "msgMovendoXmlParaImportados";
	private static final String MSG_MOVENDO_PARA_ERRO = "msgMovendoXmlParaErro";
	private static final String MSG_CRIANDO_ARQUIVO_RETORNO = "msgCriandoArquivoRetorno";
	private static final String MSG_NAO_EXISTEM_MENSAGENS = "msgNaoExistemMensagens";
}
