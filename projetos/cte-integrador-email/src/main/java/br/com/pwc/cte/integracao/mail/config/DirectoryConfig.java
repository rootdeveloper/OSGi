package br.com.pwc.cte.integracao.mail.config;

import java.io.File;
import java.util.Properties;


/**
 * Classe respons�vel por criar os diret�rios automaticamente.
 * 
 * @author sergio.moreira
 * @author daniel.santos
 *
 */
public class DirectoryConfig {

	/**
	 * M�todo respons�vel por criar os diret�rios que n�o existem.
	 * 
	 * @param propertieConfig
	 */
	public static void createDirectory(Properties propertieConfig) {
		File file = null;
		ConfigEnum[] cfgs = new ConfigEnum[3];
		cfgs[0] = ConfigEnum.INTEGRACAO_DIR_IMPORTADOS;
		cfgs[1] = ConfigEnum.INTEGRACAO_DIR_RETORNO_IMPORTACAO;
		cfgs[2] = ConfigEnum.INTEGRACAO_DIR_ERRO_INVALIDOS;
		
		for(ConfigEnum cfg : cfgs) {
			file = new File(propertieConfig.getProperty(cfg.getPropriedade()));
			if(!file.exists()) {
				file.mkdir();
			}
		}
	}
}
