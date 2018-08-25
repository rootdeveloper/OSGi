package br.com.pwc.cte.integracao.mail.config;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Singleton para controle do arquivo de mensagens
 * @author daniel.santos
 */
@Component
@Scope(value="singleton")
public class IntegradorEmailMessageFactory {
	
	private static final Logger LOGGER = Logger.getLogger(IntegradorEmailMessageFactory.class);
	
	private ResourceBundle bundle;

	/**
	 * Método chamado após a inicialização do Singleton para recuperar o properties
	 */
	@PostConstruct
	public void init() {
		LOGGER.info(INICIALIZANDO_MENSAGENS);
		bundle = ResourceBundle.getBundle("mensagensIntegradorNFe");
	}
	
	/**
	 * Recupera a string do bundle a partir da chave
	 * @param chave <br/> Da mensagem 
	 * @return Mensagem <br/> Do bundle ou null, caso não a encontre
	 */
	public String getMessage(String chave) {
		try {
			return bundle.getString(chave);
		} catch(MissingResourceException e) {
			LOGGER.error(getMessageWithParameter(ERRO_MENSAGEM_NAO_ENCONTRADA, chave), e);
			return null;
		}
	}

	/**
	 * Recupera e formata a string do bundle a partir da chave e dos parametros informados
	 * 
	 * @param chave <br/> Da mensagem
	 * @param parametros <br/> A serem colocados na mensagem
	 * @return Mensagem do bundle ou null, caso não encontre
	 */
	public String getMessageWithParameter(String chave, Object... parametros) {
		try {
			return MessageFormat.format(bundle.getString(chave), parametros);
		} catch(MissingResourceException e) {
			LOGGER.error(ERRO_MENSAGEM_NAO_ENCONTRADA, e);
			return null;
		}
	}

	private static final String ERRO_MENSAGEM_NAO_ENCONTRADA = "chave não encontrada";
	private static final String INICIALIZANDO_MENSAGENS = "inicializando o arquivo de mensagens";
	
}
