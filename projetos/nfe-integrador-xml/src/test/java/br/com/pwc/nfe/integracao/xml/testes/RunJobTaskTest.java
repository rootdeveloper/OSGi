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

import br.com.pwc.nfe.integracao.xml.RunJobTask;

public class RunJobTaskTest extends AbstractTest{
	
	@Autowired
	private RunJobTask runJobTask;
	
	@Resource(name = "configPrincipal")
	private Properties configProperties;
	
	@BeforeClass
	public static void init() {
		
	}
	
	@Test
	public void testSucessExecute () throws Exception {
		
		moveArquivoParaPastaDeEnvio();
		
		runJobTask.execute();
		
		// confirmar que o xml movido no inicio não está mais na pasta de envio
	}
	
	
	private void moveArquivoParaPastaDeEnvio() {
		
	}


	private static final String PATH_FILE_APROVADO = "src/test/resources/xmls/aprovado.xml";
	
}
