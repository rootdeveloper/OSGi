package br.com.pwc.nfe.integracao.xml.config;

import static br.com.pwc.nfe.integracao.xml.config.ConfigType.DELAY;
import static br.com.pwc.nfe.integracao.xml.config.ConfigType.FOLDER;
import static br.com.pwc.nfe.integracao.xml.config.ConfigType.VIEW;
import static br.com.pwc.nfe.integracao.xml.config.ConfigType.WS_ADDR;
import static br.com.pwc.nfe.integracao.xml.config.ConfigType.BUFFER;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum com das propriedades de configuração do sistema.
 * 
 * @author sergio.moreira
 * @author daniel.santos
 *
 */
public enum ConfigEnum {
	
	INTEGRACAO_MODO_GRAFICO("integracao.view", VIEW),
	/**
	 * Chave que recebe o valor do endpoint do portal.
	 */
	INTEGRACAO_PORTAL_IP("integracao.portal.ip", WS_ADDR),
	
	DELAY_LISTENER("delay", DELAY),
	INTERVALO_LISTENER("intervalo", DELAY),
	/**
	 * Chaves que recebem a localização das pastas do sistema.
	 */
	INTEGRACAO_DIR_ENVIO("integracao.dir.envio", FOLDER),
	INTEGRACAO_DIR_IMPORTADOS("integracao.dir.importados", FOLDER),
	INTEGRACAO_DIR_ERROS("integracao.dir.erros", FOLDER),
	INTEGRACAO_DIR_ERROS_COMUNICACAO("integracao.dir.erros.comunicacao", FOLDER),
	INTEGRACAO_DIR_INVALIDOS("integracao.dir.invalidos", FOLDER),
	INTEGRACAO_DIR_RETORNOS("integracao.dir.retornos", FOLDER),
	INTEGRACAO_QTD_MSG_BUFFER("qtd.threads.envio", BUFFER);
	
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
	
	private ConfigEnum(String key, ConfigType type) {
		this.key = key;
		this.type = type;
	}
	
	
	public String getKey() {
		return key;
	}


	/**
	 * Metodo de lookup para obter enum atraves do valor da propriedade passado.
	 * @param valor - valor da propriedade do enum.
	 * @return {@link ConfigEnum}
	 */
	public static ConfigEnum lookupPorPropriedade(String valor) {
		return mapaConfig.get(valor);
	}
	
	public ConfigType getType() {
		return type;
	}
	
	private String key;
	private ConfigType type;
}
