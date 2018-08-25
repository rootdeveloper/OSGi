package br.com.pwc.cte.integracao.mail.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.pwc.cte.integracao.mail.config.IntegradorEmailMessageFactory;

/**
 * Classe responsável por validar se o arquivo possui tags comuns a um NFe.
 * 
 * @author sergio.moreira
 * @author daniel.santos
 *
 */
@Component
@Scope(value="singleton")
public class ValidatorCTe {

	private static final Logger LOGGER = Logger.getLogger(ValidatorCTe.class);
	
	@Autowired
	private IntegradorEmailMessageFactory messageFactory;
	
	public boolean isValid(String nmFile, String xml) {
		
		LOGGER.info(messageFactory.getMessage(MSG_PROCESS_VALIDED_FILE));
		if(!nmFile.toLowerCase().endsWith(".xml")) {
			LOGGER.error(messageFactory.getMessage(MSG_FILE_IS_NOT_XML));
			return false;
		}
		if(xml == null || xml.isEmpty()) {
			return false;
		}
		return isCTe(xml);
	}
	
	/**
	 * Verifica se é um CT-e 
	 * @return
	 * @param stb
	 */
	public static boolean isCTe(String stb) {
		for(String value : tags) {
			Pattern p = Pattern.compile(value.toUpperCase());
			Matcher m = p.matcher(stb.toString().toUpperCase());
			if(!m.find()) {
				return false;
			}
		}
		return true;
	}	
	
	private static final String MSG_PROCESS_VALIDED_FILE = "Verificando se o arquivo é válido!";
	private static final String MSG_FILE_IS_NOT_XML = "Arquivo não tem extensão xml!";
	private final static String[] tags = {"infNFe", "nfeProc"};
}
