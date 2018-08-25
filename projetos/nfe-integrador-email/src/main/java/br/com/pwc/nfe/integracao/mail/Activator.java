package br.com.pwc.nfe.integracao.mail;

import java.io.IOException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.pwc.nfe.integracao.mail.config.SystemConfigurationValidator;
import br.com.pwc.nfe.integracao.mail.tray.TraySystem;


/**
 * Classe principal que inicializa o contexto do Spring
 * 
 * @author daniel.santos
 * 
 */
public class Activator implements BundleActivator {
	
	public static void main(String[] args) throws Exception {
		Activator activator = new Activator();
		activator.start(null);
		new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml");
	}

	@Override
	public void start(BundleContext context) throws Exception {
		
		SystemConfigurationValidator validator = new SystemConfigurationValidator();
		
		try {
			validator.chechConfigurations();
			
		} catch (IOException e) {
			System.out.println("IMPOSSIVEL INICIAR O SISTEMA CORRETAMENTE. CONFIGURAÇÕES INVALIDAS! " + e);
		} catch (IllegalArgumentException e) {
			System.out.println("IMPOSSIVEL INICIAR O SISTEMA CORRETAMENTE. CONFIGURAÇÕES INVALIDAS! " + e);
		}
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
		System.out.println("Finalizando Integrador de Email");
		TraySystem.removeTray();
	}
	
}
