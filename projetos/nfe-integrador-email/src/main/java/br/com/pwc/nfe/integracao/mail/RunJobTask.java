package br.com.pwc.nfe.integracao.mail;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.pwc.nfe.integracao.mail.config.Account;
import br.com.pwc.nfe.integracao.mail.config.IntegradorEmailMessageFactory;
import br.com.pwc.nfe.integracao.mail.util.FileManager;
import br.com.pwc.nfe.integracao.mail.util.XmlInspector;

/**
 * Classe que contém o job para o quartz executar.
 * 
 * @author daniel.santos
 *
 */
@Scope(value="singleton")
@Component
public class RunJobTask {

	@Autowired
	private IntegradorEmailMessageFactory messageFactory;
	
	@Autowired
	private XmlInspector xmlInspector;
	
	@Autowired
	private Integrador integrador;
	
	@Autowired
	private FileManager fileManager;
	
	@Resource(name = "configPrincipal")
	private Properties configProperties;
	
	private EmailReader[] arrayOfEmailReader;
	
	private static ExecutorService executorService;
	
	@Autowired
	private LoadAccounts loadAccounts;
	
	@PostConstruct
	public void init() {
		
		// pega as contas configuradas
		Account[] arrayOfAccounts = loadAccounts.getAccounts();
		
		// cria o array de leitores de email e o array de threads
		arrayOfEmailReader = new EmailReader[arrayOfAccounts.length];
		
		executorService = Executors.newFixedThreadPool(arrayOfAccounts.length);
		
		for(int i = 0; i < arrayOfEmailReader.length; i++) {
			arrayOfEmailReader[i] = new EmailReader(
					arrayOfAccounts[i], messageFactory, xmlInspector, integrador, fileManager, configProperties);
		}
	}
	
	public void execute() {
		
		for(int j = 0; j < arrayOfEmailReader.length; j++) {
			
			executorService.execute(arrayOfEmailReader[j]);
		}
    }
	
	public static ExecutorService getExecutorService() {
		return executorService;
	}

}
