package br.com.pwc.cte.integracao.mail.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum com as propriedades de configuração do sistema.
 * 
 * @author sergio.moreira
 *
 */
public enum ConfigEnum {
	
	INTEGRACAO_MODO_GRAFICO("integracao.view"),
	/**
	 * Configurações de acesso ao portal.
	 */
	INTEGRACAO_PORTAL_IP("integracao.portal.ip"),
	INTEGRACAO_DIR_IMPORTADOS("integracao.dir.importados"),
	INTEGRACAO_DIR_ERRO_INVALIDOS("integracao.dir.erro.invalidos"),
	INTEGRACAO_DIR_RETORNO_IMPORTACAO("integracao.dir.retorno.importacao"),
	
	/**
	 * Nome da pasta a ser criada no email 
	 * OBS:Email para criação de pasta tem de ser imap.
	 */
	EMAIL_NAME_FOLDER("mail.folder.name"),
	/**
	 * Parametros de serviço de email.
	 * 
	 */
	CONTA_DEBUG_PROCESSO("mail.debug"),
	CONTA_DEBUG_AUTHENTICATOR("mail.debug.auth"),
		
	/**
	 * Dados de configuração de contas de email.
	 */
	QTDE_CONTAS("conta.qtde"),
	CONTA_PROTOCOLO("conta.protocolo"),
	CONTA_USER("conta.user"),
	CONTA_PASSWD("conta.passwd"),
	CONTA_SERVER("conta.host"),
	CONTA_PORT("conta.port"),
	CONTA_SSL("conta.ssl"),
	CONTA_TIMEOUT("conta.timeout");
	
	private static Map<String, ConfigEnum> mapaConfig = new HashMap<String, ConfigEnum>();
	
	static {
		for(ConfigEnum config : values()) {
			mapaConfig.put(config.getPropriedade(), config);
		}
	}
	
	private ConfigEnum(String valor) {
		this.propriedade = valor;
	}
	
	
	public String getPropriedade() {
		return propriedade;
	}


	/**
	 * Metodo de lookup para obter enum atraves do valor da propriedade passado.
	 * @param valor - valor da propriedade do enum.
	 * @return {@link ConfigEnum}
	 */
	public static ConfigEnum lookupPorPropriedade(String valor) {
		return mapaConfig.get(valor);
	}
	
	private String propriedade;
	
}
