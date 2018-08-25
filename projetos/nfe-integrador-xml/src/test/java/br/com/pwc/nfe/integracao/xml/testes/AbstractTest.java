package br.com.pwc.nfe.integracao.xml.testes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.rules.TestWatchman;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * Classe abstrata para configuracoes padroes de testes
 * 
 * @author daniel.santos
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath:spring/applicationContext.xml"})
public abstract class AbstractTest extends AbstractTestNGSpringContextTests {
	
	private static Logger LOGGER = Logger.getLogger(AbstractTest.class);
	
	@Rule
	public MethodRule watchman = new TestWatchman() {
		public void starting(FrameworkMethod method) {
			System.out.println("Executando teste: " + method.getName());
		}
	};
	
	@BeforeClass
	public static void setUp() throws IOException, SQLException {
		prepararBundle();
		System.out.println("Iniciando spring context...");
//		classPathXmlApplicationContext = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
	} 

	/**
	 * Copia o bundle do main para o test
	 * 
	 * @throws IOException <br/> Caso ocorra algum erro ao copiar
	 */
	private static void prepararBundle() throws IOException {
		FileUtils.copyFile(
			new File("src/main/resources/mensagensIntegradorXmlNFe.properties"),
			new File(TARGET_DIR + "mensagensIntegradorXmlNFeTest.properties"));
	}

	/**
	 * Pega um arquivo e retorna uma string com o conteúdo
	 * @param path do arquivo
	 * @return {@link String} - conteúdo do arquivo
	 */
	protected String getArquivo(String path) {
		String str = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					new File(path)));

			while(in.ready()) {
				str += in.readLine();
			}
			in.close();
		} catch(IOException e) {
			
		}
		return str;
	}
	
	@AfterClass
	public static void finalizaContexto() {
		System.out.println("Finalizando spring context");
//		classPathXmlApplicationContext.destroy();
	}
	
	private static final String TARGET_DIR = "src/test/resources/";
	private static ClassPathXmlApplicationContext classPathXmlApplicationContext;
}
