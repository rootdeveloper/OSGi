package br.com.pwc.nfe.integracao.mail.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum com  tidos de protocolos utilizados por um email.
 * @author sergio.moreira
 *
 */
public enum ProtocolEmailEnum {

	IMAP("imap"),
	IMAPS("imaps"),
	POP("pop"),
	POP3("pop3"),
	POP3S("pop3s"),
	SMTP("smtp"),
	SMTPS("smtps");
	
	private static Map<String, ProtocolEmailEnum> mapaProtocolo = new HashMap<String, ProtocolEmailEnum>();
	
	static {
		for(ProtocolEmailEnum p : values()) {
			mapaProtocolo.put(p.getProtocolo(), p);
		}
	}

	private ProtocolEmailEnum(String protocolo) {
		this.protocolo = protocolo;
	}
	
	public String getProtocolo() {
		return protocolo;
	}

	/**
	 * Lookup para obter o protocolo pela propriedade.
	 * @param valor - propriedade.
	 * @return {@link ProtocoloEmail}
	 */
	public static ProtocolEmailEnum lookupPorProtocolo(String valor) {
		return mapaProtocolo.get(valor);
	}

	private String protocolo;

}
