package br.com.pwc.nfe.integracao.xml.testes;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.pwc.nfe.integracao.xml.Integrador;
import br.com.pwc.nfe.integracao.xml.config.ConfigEnum;
import br.com.pwc.nfe.integracao.xml.util.IntegradorException;
import br.com.pwc.webservice.communicator.http.HttpCommunicator;
import br.com.pwc.webservice.communicator.http.HttpCommunicatorException;
import br.com.pwc.webservice.communicator.http.vo.HttpCommunicatorRequestVO;

public class IntegradorTest extends AbstractTest{
	
	@Autowired
	private Integrador integrador;
	
	@Resource(name = "configPrincipal")
	private Properties configProperties;
	
	@BeforeClass
	public static void init() {
		
		httpCommunicator = mock(HttpCommunicator.class);
	}
	
	@Test
	public void testSucessEnviaSolicitacao () throws Exception {
		String xml = criaXml(PATH_FILE_APROVADO);
		String retorno = integrador.enviaSolicitacao(xml);
		Assert.assertNotNull(retorno);
		
	}
	
	//  TODO Ainda não consegui fazer o doThrow ou o thenThrow levantar a exception esperada
	@Test(expected = IntegradorException.class)
	public void testErrorEnviaSolicitacao() throws Exception {
		String xml = criaXml(PATH_FILE_APROVADO);
		integrador.setHttpCommunicator(httpCommunicator);
		HttpCommunicatorRequestVO httpVO = criaHttpVO(xml);
//		doThrow(new HttpCommunicatorException()).when(httpCommunicator).comunica(httpVO);
		when(httpCommunicator.comunica(httpVO)).thenThrow(new HttpCommunicatorException());
		integrador.enviaSolicitacao(xml);
	}
	
	private HttpCommunicatorRequestVO criaHttpVO(String xml) throws IOException {
		
		HttpCommunicatorRequestVO httpVO = new HttpCommunicatorRequestVO();
		httpVO.setLocationURI(configProperties.getProperty(ConfigEnum.INTEGRACAO_PORTAL_IP.getKey()));
		//poderia ser um xml qualquer para preencher a mensagem
		httpVO.setMensagem(criaXml(PATH_FILE_APROVADO));
		
		return httpVO;
	} 
	
	private String criaXml(String path) throws IOException {
		String xml = FileUtils.readFileToString(new File(path));
		return xml;
	}
	
	
	private static HttpCommunicator httpCommunicator;
	
	private static final String PATH_FILE_APROVADO = "src/test/resources/xmls/aprovado.xml";
	
}
