package br.com.pwc.nfe.integracao.xml.util;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.log4j.Logger;

/**
 * Classe para filtro de arquivos
 * @author daniel.santos
 *
 */
public class FilenameFilterImpl implements FilenameFilter {
	
	private Logger LOGGER = Logger.getLogger(FilenameFilterImpl.class);
	
	private String suffix;
	
	public FilenameFilterImpl(String suffix) {
		this.suffix = suffix;
	}
	
	public boolean accept(File file, String name) {
		
		try{
			String nomeMinusculo = name.toLowerCase();
			
			if(!nomeMinusculo.endsWith(suffix)){
				return true;
			}
		}catch (Exception e) {
			LOGGER.error(ERRO_AO_VERIFICAR_NOME, e);
		}
		return false;
	}
	
	private static final String ERRO_AO_VERIFICAR_NOME = "msgErroAoVerificarNome";
}
