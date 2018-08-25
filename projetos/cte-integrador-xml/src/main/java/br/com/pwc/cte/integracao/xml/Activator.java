package br.com.pwc.cte.integracao.xml;

import java.io.IOException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.pwc.cte.integracao.xml.config.SystemConfigurationValidator;
import br.com.pwc.cte.integracao.xml.tray.TraySystem;


/**
 * Classe principal que inicializa o contexto do Spring
 * 
 * @author daniel.santos
 * 
 */
public class Activator implements BundleActivator {
	
	private static boolean configValid;

	public static void main(String[] args) throws Exception {
		configValid = true;
		Activator activator = new Activator();
		activator.start(null);
		if(configValid) {
			new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml");
		}
	}
	
	public void start(BundleContext context) throws Exception {
		
		SystemConfigurationValidator validator = new SystemConfigurationValidator();
		
		try {
			validator.chechConfigurations();
			
		} catch (IOException e) {
			Activator.configValid = false;
			this.stop(null);
			System.out.println("IMPOSSIVEL INICIAR O SISTEMA CORRETAMENTE. CONFIGURAÇÕES INVALIDAS! " + e);
		} catch (IllegalArgumentException e) {
			Activator.configValid = false;
			this.stop(null);
			System.out.println("IMPOSSIVEL INICIAR O SISTEMA CORRETAMENTE. CONFIGURAÇÕES INVALIDAS! " + e);
		}
	}

	public void stop(BundleContext context) throws Exception {
		
		System.out.println("Encerrando Integrador XML...");
		TraySystem.removeTray();
		RunJobTask.getExecutorService().shutdown();
	}
	
}
