package br.com.pwc.nfe.integracao.mail.config;

/**
 * Enum de configurações da conta, utilizado para configurar contas
 * ja que existem configurações comuns aos dois protocolos, fornece
 * as propriedades comuns aos dois e suas configurações particulares.
 * @author sergio.moreira
 *
 */
public enum ParameterEmailEnum {

	/**
	 * Parametros de configuração por imap.
	 */
	MAIL_DEBUG("mail.debug"),
	MAIL_DEBUG_AUTHENTICATOR("mail.debug.auth"),
	MAIL_USER("mail.user"),
	MAIL_HOST("mail.host"),
	MAIL_STORE_PROVIDER("mail.store.protocol"),
	
	IMAP_SSL("mail.imap.ssl.enable"),
	IMAP_PORT("mail.imap.port"),
	IMAP_TIMEOUT("mail.imap.timeout"),
	IMAP_SOCKECT_FACTORY("mail.imap.socketFactory.class"),
	IMAP_SOCKECT_FACTORY_PORT("mail.imap.socketFactory.port"),
	
	POP3_SSL("mail.pop3.ssl.enable"),
	POP3_PORT("mail.pop3.port"),
	POP3_TIMEOUT("mail.pop3.timeout"),
	POP3_SOCKECT_FACTORY("mail.pop3.socketFactory.class"),
	POP3_SOCKECT_FACTORY_PORT("mail.pop3.socketFactory.port");
	
	
	/**
	 * Construtor privado que recebe o valor da propriedade.
	 * @param valor - propriedade.
	 */
	private ParameterEmailEnum(String valor) {
		this.valor = valor;
	}

	private String valor;

	public String getValor() {
		return valor;
	}
}
