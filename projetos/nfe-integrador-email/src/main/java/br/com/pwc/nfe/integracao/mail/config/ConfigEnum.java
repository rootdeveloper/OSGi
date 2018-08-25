package br.com.pwc.nfe.integracao.mail.config;

import java.util.HashMap;
import java.util.Map;

import static br.com.pwc.nfe.integracao.mail.config.ConfigType.DELAY;
import static br.com.pwc.nfe.integracao.mail.config.ConfigType.FOLDER;
import static br.com.pwc.nfe.integracao.mail.config.ConfigType.ITEM_EMAIL;
import static br.com.pwc.nfe.integracao.mail.config.ConfigType.QTD_EMAILS;
import static br.com.pwc.nfe.integracao.mail.config.ConfigType.VIEW;
import static br.com.pwc.nfe.integracao.mail.config.ConfigType.WS_ADDR;
import static br.com.pwc.nfe.integracao.mail.config.ConfigType.PROP_EMAIL;

/**
 * Enum com das propriedades de configuração do sistema.
 * 
 * @author sergio.moreira
 * @author daniel.santos
 *
 */
public enum ConfigEnum {
	
	INTEGRACAO_MODO_GRAFICO("integracao.view", VIEW),

	INTEGRACAO_PORTAL_IP("integracao.portal.ip", WS_ADDR),
	
	DELAY_LISTENER("delay", DELAY),
	INTERVALO_LISTENER("intervalo", DELAY),
	
	/**
	 * Chaves que recebem a localização das pastas do sistema.
	 */
	INTEGRACAO_DIR_IMPORTADOS("integracao.dir.importados", FOLDER),
	INTEGRACAO_DIR_ERROS("integracao.dir.erros", FOLDER),
	INTEGRACAO_DIR_ERROS_COMUNICACAO("integracao.dir.erros.comunicacao", FOLDER),
	INTEGRACAO_DIR_INVALIDOS("integracao.dir.invalidos", FOLDER),
	INTEGRACAO_DIR_RETORNOS("integracao.dir.retornos", FOLDER),
	
	/**
	 * Parametros de serviço de email.
	 * 
	 */
	CONTA_DEBUG_PROCESSO("mail.debug", PROP_EMAIL),
	CONTA_DEBUG_AUTHENTICATOR("mail.debug.auth", PROP_EMAIL),
	EMAIL_NAME_FOLDER("mail.folder.name", PROP_EMAIL),
	QTDE_CONTAS("conta.qtde", QTD_EMAILS),
		
	/**
	 * Dados de configuração de contas de email.
	 */
	CONTA_PROTOCOLO("conta.protocolo", ITEM_EMAIL),
	CONTA_USER("conta.user", ITEM_EMAIL),
	CONTA_PASSWD("conta.passwd", ITEM_EMAIL),
	CONTA_SERVER("conta.host", ITEM_EMAIL),
	CONTA_PORT("conta.port", ITEM_EMAIL),
	CONTA_SSL("conta.ssl", ITEM_EMAIL),
	CONTA_TIMEOUT("conta.timeout", ITEM_EMAIL);
	
	private static Map<String, ConfigEnum> mapaConfig = new HashMap<String, ConfigEnum>();
	
	static {
		for(ConfigEnum config : values()) {
			mapaConfig.put(config.getKey(), config);
		}
	}
	
	public static ConfigEnum convert(String value) {
		ConfigEnum[] values = values();
		for (ConfigEnum config : values) {
			if (config.key.equals(value))
				return config;
		}
		throw new IllegalArgumentException("Propriedade desconhecida: " + value);
	}
	
	private ConfigEnum(String valor, ConfigType type) {
		this.key = valor;
		this.type = type;
	}
	
	public String getKey() {
		return key;
	}
	
	public ConfigType getType() {
		return type;
	}

	/**
	 * Metodo de lookup para obter enum atraves do valor da propriedade passado.
	 * @param valor - valor da propriedade do enum.
	 * @return {@link ConfigEnum}
	 */
	public static ConfigEnum lookupPorPropriedade(String valor) {
		return mapaConfig.get(valor);
	}
	
	private String key;
	private ConfigType type;
	
}
