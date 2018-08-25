package br.com.pwc.nfe.integracao.mail.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.pwc.nfe.integracao.mail.config.IntegradorEmailMessageFactory;

/**
 * Classe para que concentra a manipulação de arquivos
 * @author daniel.santos
 *
 */
@Component
public class FileManager {
	
	private static Logger LOGGER = Logger.getLogger(FileManager.class);
	
	@Autowired
	private IntegradorEmailMessageFactory messageFactory;
	
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
	
	public String getRealName(File file) {
		String name = file.getName().substring(0, file.getName().length()-1);
		return name;
	}
	
	private static final String MSG_ERRO_AO_MOVER =  "msgErroAoMover";
	private static final String MSG_ERRO_AO_CRIAR =  "msgErroAoCriarArquivo";

}