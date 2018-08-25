package br.com.pwc.nfe.integracao.mail.config;

import java.util.Properties;

import org.springframework.stereotype.Component;

/**
 * Classe responsável por obter e montar mapa de configuração de contas.
 * 
 * @author daniel.santos
 *
 */
@Component
public class ConfiguraConta {

	/**
	 * Método que transforma os dados da conta em um propertie de configuração
	 * de email.
	 * @param conta - Objeto conta.
	 * @return {@link Properties}
	 */
	public static Properties getConfig(Account conta) {
		Properties cfg = new Properties();
		
		if(conta.getProtocolo() == ProtocolEmailEnum.IMAP.getProtocolo() || conta.getProtocolo() == ProtocolEmailEnum.IMAPS.getProtocolo()) {
			
			cfg.put(ParameterEmailEnum.MAIL_STORE_PROVIDER.getValor(), conta.getProtocolo());
			cfg.put(ParameterEmailEnum.MAIL_USER.getValor(), conta.getUser());
			cfg.put(ParameterEmailEnum.MAIL_HOST.getValor(), conta.getServer());
			cfg.put(ParameterEmailEnum.MAIL_DEBUG.getValor(), conta.isDebugMail());
			cfg.put(ParameterEmailEnum.MAIL_DEBUG_AUTHENTICATOR.getValor(), conta.isDebugAuthenticador());
			if(conta.isSsl()) {
				cfg.put(ParameterEmailEnum.IMAP_SSL.getValor(), conta.isSsl());
				cfg.put(ParameterEmailEnum.IMAP_SOCKECT_FACTORY.getValor(), conta.getSocketFactorySSL());
				cfg.put(ParameterEmailEnum.IMAP_SOCKECT_FACTORY_PORT.getValor(), conta.getSocketFacotorySSLPort());
			}
			cfg.put(ParameterEmailEnum.IMAP_PORT.getValor(), conta.getPort());
			cfg.put(ParameterEmailEnum.IMAP_TIMEOUT.getValor(), conta.getTimeOut());
		
		}else {
			
			cfg.put(ParameterEmailEnum.MAIL_STORE_PROVIDER.getValor(), conta.getProtocolo());
			cfg.put(ParameterEmailEnum.MAIL_USER.getValor(), conta.getUser());
			cfg.put(ParameterEmailEnum.MAIL_HOST.getValor(), conta.getServer());
			cfg.put(ParameterEmailEnum.MAIL_DEBUG.getValor(), conta.isDebugMail());
			cfg.put(ParameterEmailEnum.MAIL_DEBUG_AUTHENTICATOR.getValor(), conta.isDebugAuthenticador());
			if(conta.isSsl()) {
				cfg.put(ParameterEmailEnum.POP3_SSL.getValor(), conta.isSsl());
				cfg.put(ParameterEmailEnum.POP3_SOCKECT_FACTORY.getValor(), conta.getSocketFactorySSL());
				cfg.put(ParameterEmailEnum.POP3_SOCKECT_FACTORY_PORT.getValor(), conta.getSocketFacotorySSLPort());
			}
			cfg.put(ParameterEmailEnum.POP3_PORT.getValor(), conta.getPort());
			cfg.put(ParameterEmailEnum.POP3_TIMEOUT.getValor(), conta.getTimeOut());
		}
		return cfg;
	}
	
}
