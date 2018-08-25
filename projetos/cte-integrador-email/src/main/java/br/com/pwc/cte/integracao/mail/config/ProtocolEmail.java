package br.com.pwc.cte.integracao.mail.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum com  tidos de protocolos utilizados por um email.
 * @author sergio.moreira
 *
 */
public enum ProtocolEmail {

	IMAP("imap"),
	IMAPS("imaps"),
	POP("pop"),
	POP3("pop3"),
	POP3S("pop3s"),
	SMTP("smtp"),
	SMTPS("smtps");
	
	private static Map<String, ProtocolEmail> mapaProtocolo = new HashMap<String, ProtocolEmail>();
	
	static {
		for(ProtocolEmail p : values()) {
			mapaProtocolo.put(p.getProtocolo(), p);
		}
	}

	private ProtocolEmail(String protocolo) {
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
	public static ProtocolEmail loocupPorProtocolo(String valor) {
		return mapaProtocolo.get(valor);
	}

	private String protocolo;

}
