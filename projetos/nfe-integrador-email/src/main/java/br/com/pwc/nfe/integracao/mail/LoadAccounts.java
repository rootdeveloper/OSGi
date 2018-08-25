package br.com.pwc.nfe.integracao.mail;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.pwc.nfe.integracao.mail.config.Account;
import br.com.pwc.nfe.integracao.mail.config.ConfigEnum;
import br.com.pwc.nfe.integracao.mail.config.IntegradorEmailMessageFactory;


/**
 * Classe singleton responsável por carregar e mapear contas cadastradas.
 * 
 * @author sergio.moreira
 * @author daniel.santos
 *
 */
@Component
@Scope(value="singleton")
public class LoadAccounts {
	
	private static final Logger LOGGER = Logger.getLogger(LoadAccounts.class);
	
	@Autowired
	private IntegradorEmailMessageFactory messageFactory;
	
	private Properties configProperties;
	
	private static Account[] arrayOfAccounts;
	
	public LoadAccounts() {}
	
	/**
	 * Construtor publico que recebe properties de configuração,
	 * chama os metodos que carregam e mapeiam as contas para inicialização dos sistema.
	 * @param valorProperties
	 */
	public LoadAccounts(Properties valorProperties) {
		configProperties = valorProperties;
	}
	
	/**
	 * Método responsavel por ler config properties de sistemas e mapear as contas,
	 * e separa-las em pop e imap.
	 */
	public void loadConfig() {
		
		LOGGER.info(messageFactory.getMessage(MSG_QUANTIDADE_CONTAS));
		Integer quantidadeDeContas = Integer.valueOf(configProperties.getProperty(ConfigEnum.QTDE_CONTAS.getKey()));
		
		arrayOfAccounts = new Account[quantidadeDeContas];
		
		for (int i = 0; i < quantidadeDeContas; i++) {
			
			Account account = new Account();
			
			account.setDebugMail(Boolean.valueOf(configProperties.getProperty(ConfigEnum.CONTA_DEBUG_PROCESSO.getKey())));                                           
			account.setDebugAuthenticador(Boolean.valueOf(configProperties.getProperty(ConfigEnum.CONTA_DEBUG_AUTHENTICATOR.getKey())));                     
			
			account.setUser(configProperties.getProperty(ConfigEnum.CONTA_USER.getKey().concat("." + i)));
			account.setPasswd(configProperties.getProperty(ConfigEnum.CONTA_PASSWD.getKey().concat("." + i)));                   
			account.setPort(Integer.valueOf(configProperties.getProperty(ConfigEnum.CONTA_PORT.getKey().concat("." + i))));      
			
			account.setSocketFactorySSL(SOCKECT_FACTORY_VALUE);                                             
			account.setSocketFacotorySSLPort(String.valueOf(configProperties.getProperty(ConfigEnum.CONTA_PORT.getKey().concat("." + i))));                            
			
			account.setServer(configProperties.getProperty(ConfigEnum.CONTA_SERVER.getKey().concat("." + i)));                   
			account.setSsl(Boolean.valueOf(configProperties.getProperty(ConfigEnum.CONTA_SSL.getKey().concat("." + i))));
			account.setTimeOut(Long.valueOf(configProperties.getProperty(ConfigEnum.CONTA_TIMEOUT.getKey().concat("." + i))));
			account.setProtocolo(configProperties.getProperty(ConfigEnum.CONTA_PROTOCOLO.getKey().concat("." + i)));             
			
			arrayOfAccounts[i] = account;
		}
	}
	
	public Account[] getAccounts() {
		return arrayOfAccounts;
	}

	private static final String SOCKECT_FACTORY_VALUE = "javax.net.SocketFactory";
	private static final String MSG_QUANTIDADE_CONTAS = "msgObtendoQtdContas";
}