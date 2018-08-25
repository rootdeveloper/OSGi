package br.com.pwc.cte.integracao.mail.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.pwc.cte.integracao.mail.config.ConfigEnum;
import br.com.pwc.cte.integracao.mail.config.IntegradorEmailMessageFactory;

/**
 * Classe gerenciadora de arquivos.
 * 
 * @author daniel.santos
 *
 */
@Component
public class MoveFileUtils {

	private static final Logger LOGGER = Logger.getLogger(MoveFileUtils.class);
	
	@Autowired
	private IntegradorEmailMessageFactory messageFactory;
	
	/**
	 * Codigos de identificação de status.
	 */
	public static final int SUCESSO = 0;
	public static final int ARQUIVO_INVALIDO = 1;
	public static final int ERRO_IMPORTACAO = 2;

	/**
	 * Atributos de identificação de diretórios.
	 */
	private static String dirImported;
	private static String dirError;
	private static String dirErrorImportaded;
	
	/**
	 * Método que gerencia para qual layout de pasta o arquivo será movido.
	 * @param cod Codigo de retorno para identificar o diretório.
	 * @param xml Xml a ser movido.
	 * @param nmArq Nome do arquivo para identificação em disco.
	 * @param configProperties Config com as propriedades dos sistema para obter caminho do diretório.
	 * @throws MoveFileUtilsException Caso ocorrer algum problema no gerenciamento.
	 */
	public void moveFile(int cod, String xml, String nmArq, Properties configProperties)
			throws MoveFileUtilsException {
		
		dirImported = configProperties.getProperty(ConfigEnum.INTEGRACAO_DIR_IMPORTADOS.getPropriedade());
		dirError = configProperties.getProperty(ConfigEnum.INTEGRACAO_DIR_ERRO_INVALIDOS.getPropriedade());
		dirErrorImportaded = configProperties.getProperty(ConfigEnum.INTEGRACAO_DIR_RETORNO_IMPORTACAO.getPropriedade());
		try {
			switch (cod) {
			case 0:
				LOGGER.info(messageFactory.getMessage(MSG_INFO_MOV_XML_IMPORTADOS));
				FileUtils.writeStringToFile(new File(dirImported.concat("/").concat(nmArq)), xml);
				break;
			case 1:
				LOGGER.info(messageFactory.getMessage(MSG_INFO_MOV_XML_ERRO));
				FileUtils.writeStringToFile(new File(dirError.concat("/").concat(nmArq)), xml);
				break;
			case 2:
				LOGGER.info(messageFactory.getMessage(MSG_INFO_MOV_XML_ERRO_IMPORTACAO));
				FileUtils.writeStringToFile(new File(dirErrorImportaded.concat("/").concat(nmArq)), xml);
				break;
			default:
				LOGGER.info(messageFactory.getMessage(MSG_ERRO_MOV_XML));
				throw new MoveFileUtilsException(MSG_ERRO_MOV_XML);

			}
			LOGGER.info(messageFactory.getMessage(MSG_SUCESSO_MOV_XML));
		} catch (IOException e) {
			LOGGER.error(messageFactory.getMessage(MSG_ERRO_OBTER_DADOS_DIRETORIO));
		}
	}

	private static final String MSG_INFO_MOV_XML_IMPORTADOS = "msgMovendoXmlParaPastaImportados";
	private static final String MSG_INFO_MOV_XML_ERRO_IMPORTACAO = "msgMovendoXmlParaErroImportacao";
	private static final String MSG_INFO_MOV_XML_ERRO = "msgMovendoXmlParaPastaErros";
	private static final String MSG_ERRO_MOV_XML = "msgErroAoMoverArquivo";
	private static final String MSG_ERRO_OBTER_DADOS_DIRETORIO = "msgErroAbrirDiretorio";
	private static final String MSG_SUCESSO_MOV_XML = "msgArquivoMovido";
}
