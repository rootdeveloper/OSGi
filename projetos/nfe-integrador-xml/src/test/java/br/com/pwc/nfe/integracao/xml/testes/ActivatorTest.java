package br.com.pwc.nfe.integracao.xml.testes;

import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.pwc.nfe.integracao.xml.Activator;

public class ActivatorTest extends AbstractTest{
	
	private static Activator activator;
	
	@BeforeClass
	public static void init() {
		
		activator = new Activator();
	}
	
	@Test
	public void testSucessStart () throws Exception {
		
		activator.start(null);
		
		// TODO validar start do activator
	}
	
	


	
}
