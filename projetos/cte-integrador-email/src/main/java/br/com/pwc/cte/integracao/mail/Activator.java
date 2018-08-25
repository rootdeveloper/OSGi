package br.com.pwc.cte.integracao.mail;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Classe principal que inicializa o contexto do Spring
 * 
 * @author daniel.santos
 * 
 */
public class Activator implements BundleActivator {

	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml");
	}

	public void start(BundleContext arg0) throws Exception {
		
	}

	public void stop(BundleContext arg0) throws Exception {
		
	}
}
