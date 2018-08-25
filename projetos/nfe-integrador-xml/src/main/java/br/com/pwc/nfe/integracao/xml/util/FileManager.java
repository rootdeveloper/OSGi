package br.com.pwc.nfe.integracao.xml.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.pwc.nfe.integracao.xml.config.ConfigEnum;
import br.com.pwc.nfe.integracao.xml.config.IntegradorXmlMessageFactory;

/**
 * Classe para que concentra a manipulação de arquivos
 * @author daniel.santos
 *
 */
@Component
@Scope(value="singleton")
public class FileManager {
	
	private static Logger LOGGER = Logger.getLogger(FileManager.class);
	
	@Autowired
	private IntegradorXmlMessageFactory messageFactory;
	
	@Resource(name = "configPrincipal")
	private Properties configProperties;
	
	@Autowired
	private XmlInspector inspector;
	
	private File pastaDeEnvio;
	
	private File pastaErroDeComunicacao;
	
	public void configuraPastasDoSistema() {
		
		pastaErroDeComunicacao = new File(configProperties.getProperty(ConfigEnum.INTEGRACAO_DIR_ERROS_COMUNICACAO.getKey()));
		
		// Se algum arquivo ficou na pasta de envio como temporário retira a notação para poder ser executado
		pastaDeEnvio = new File(configProperties.getProperty(ConfigEnum.INTEGRACAO_DIR_ENVIO.getKey()));
		
		// Movo os arquivos de erro de comunicação para a pasta de envio
		File[] arquivos = pastaErroDeComunicacao.listFiles();
		moveAllFiles(arquivos, pastaDeEnvio);
		
		
		File[] files = pastaDeEnvio.listFiles();
		
		for(File file: files) {
			
			if(file.getName().endsWith("~")) {
				removeTemporario(file);
			}
		}
	}
	
	public void moveFile(File origem, File dest) {
		try {
			if( dest.exists() ) {
				dest.delete();
			} if(origem.exists()){
				
				FileUtils.moveFile(origem, dest);
			}
			
		} catch (IOException e) {
			LOGGER.error(messageFactory.getMessageWithParameter(MSG_ERRO_AO_MOVER, origem.getName()), e);
		}
	}
	
	public void moveAllFiles(File[] fileArray, File destino) {
		
		for(File file: fileArray) {
			File newFile = file(destino.getAbsolutePath(), file.getName());
			moveFile(file, newFile);
		}
	}
	
	public File file(String dir,String filename) {
		File fileRetorno;
		if( filename.endsWith("~") ) {
			filename = filename.substring(0, filename.length()-1);
		}
		
		if( dir.endsWith(String.valueOf(File.separatorChar)) ) {
			fileRetorno = new File(dir + filename);
			if (fileRetorno.exists()) {
				fileRetorno.delete();
			}
			return fileRetorno;
		} else {
			fileRetorno = new File(dir + File.separatorChar + filename);
			if (fileRetorno.exists()) {
				fileRetorno.delete();
			}
			return fileRetorno;	
		}
	}

	public void writeFile(File file, String conteudo) {
		try {
			FileUtils.writeStringToFile(file, conteudo, "UTF-8");
		} catch (IOException e) {
			LOGGER.error(messageFactory.getMessageWithParameter(MSG_ERRO_AO_CRIAR, file.getName()), e);
		}
	}
	
	public void removeFile(File file) {
		
		if(file.getName().endsWith("~")) {
			file.delete();
		}
	}
	
	public void removeTemporario(File file) {
		if(file.getName().endsWith("~") ) {
			file.renameTo(new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().length()-1)));
		}
	}
	
	public synchronized File getXmlOfNFeToSend() {
		
		File retorno = null;
		
		LOGGER.info(messageFactory.getMessage(LISTANDO_ARQUIVOS_DE_ENVIO));
		File[] files = pastaDeEnvio.listFiles(new FilenameFilterImpl(SUFFIX));
		
		for(File file: files) {
			
			// Se o arquivo é inválido move para pasta de inválidos
			if (!inspector.isValid(file)) {
				File destino = file(configProperties.getProperty(
						ConfigEnum.INTEGRACAO_DIR_INVALIDOS.getKey()), file.getName());
				
				LOGGER.info(messageFactory.getMessage(MSG_MOVENDO_PARA_INVALIDO) + " " + file.getName());
				moveFile(file, destino);
			
			} else {
				File newFile = new File(file.getAbsolutePath() + SUFFIX);
				file.renameTo(newFile);
				retorno = newFile;
				break;
			}
		}
		
		return retorno;
	}
	
	private static final String MSG_ERRO_AO_MOVER =  "msgErroAoMover";
	private static final String MSG_ERRO_AO_CRIAR =  "msgErroAoCriarArquivo";
	
	private final static String SUFFIX = "~";
	private static final String LISTANDO_ARQUIVOS_DE_ENVIO = "msgListandoArquivos";
	private static final String MSG_MOVENDO_PARA_INVALIDO = "msgMovendoXmlParaInvalidos";

}