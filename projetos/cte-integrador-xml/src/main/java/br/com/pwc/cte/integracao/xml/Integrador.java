package br.com.pwc.cte.integracao.xml;

import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.pwc.cte.connector.CTeEntradaImportacao;
import br.com.pwc.cte.connector.vo.CTeEntradaConnectorImportacaoRequestVO;
import br.com.pwc.cte.connector.vo.CTeEntradaConnectorImportacaoResponseVO;
import br.com.pwc.cte.integracao.xml.config.ConfigEnum;
import br.com.pwc.cte.integracao.xml.config.IntegradorXmlMessageFactory;
import br.com.pwc.cte.integracao.xml.util.IntegradorException;
import br.com.pwc.webservice.builder.soap.SoapBuilder;
import br.com.pwc.webservice.builder.soap.enums.SoapVersionEnum;
import br.com.pwc.webservice.builder.soap.vo.SoapBuilderRequestVO;
import br.com.pwc.webservice.builder.soap.vo.SoapBuilderResponseVO;
import br.com.pwc.webservice.communicator.http.HttpCommunicator;
import br.com.pwc.webservice.communicator.http.vo.HttpCommunicatorRequestVO;

/**
 * Classe responsável pelo envio do xml para o portal.
 * 
 * @author sergio.moreira
 * @author daniel.santos
 *
 */
@Component
public class Integrador {
	
	private Logger LOGGER = Logger.getLogger(Integrador.class);
	
	@Autowired(required = true)
	private CTeEntradaImportacao cteEntradaImportacao;
	
	@Autowired
	private SoapBuilder soapBuilder;
	
	@Autowired
	private HttpCommunicator httpCommunicator;
	
	@Autowired
	private CTeEntradaConnectorImportacaoRequestVO requestVO;
	
	@Resource(name = "configPrincipal")
	private Properties configProperties;
	
	@Autowired
	private IntegradorXmlMessageFactory messageFactory;
	
	/**
	 * Método responsavel por invocar connect de ws e importar/retornar o status da importação.
	 * @param voImport Objeto Vo de importação.
	 * @return {@link String} String de retorno.
	 * @throws Exception  Caso houver erro na importação.
	 */
	public String enviaSolicitacao(String xml) throws IntegradorException{
		
		try {
			LOGGER.info(messageFactory.getMessage(MSG_PROCESSO_CONSUMO_WS));
			String retorno = null;
			LOGGER.info(messageFactory.getMessage(MSG_INFO_CRIANDO_OBJETO_PARSER_ENVIO));
			requestVO.setXml(xml);
			LOGGER.info(messageFactory.getMessage(MSG_PROCESSO_SOLICITACAO_CONSUMO_WS));
			CTeEntradaConnectorImportacaoResponseVO mensagemConnector = cteEntradaImportacao.iniciaImportacao(requestVO);
			SoapBuilderRequestVO soapVO = parseNFeEntradaResponseToSoapBuilderRequest(mensagemConnector);
			LOGGER.info(messageFactory.getMessage(MSG_INFO_OBTER_ENDPOINT));
			soapVO.setWsdl(configProperties.getProperty(ConfigEnum.INTEGRACAO_PORTAL_IP.getKey()));
			LOGGER.info(messageFactory.getMessage(MSG_SUCESSO_OBTER_ENDPOINT));
			SoapBuilderResponseVO mensagemSoap = soapBuilder.construirMensagem(soapVO);
			HttpCommunicatorRequestVO httpVO = parseSoapBuilderResponseToHttpCommunicatorRequest(mensagemSoap);
			retorno = httpCommunicator.comunica(httpVO);
			LOGGER.info(messageFactory.getMessage(MSG_RETORNO_CONSUMO_WS));
			LOGGER.info(messageFactory.getMessage(MSG_SUCESSO_CONSUMO_WS));
			
			return retorno;
		}catch(Exception e) {
			LOGGER.error("Erro ao solicitar importação", e);
			throw new IntegradorException(e);
		}
		
	}
	
	private HttpCommunicatorRequestVO parseSoapBuilderResponseToHttpCommunicatorRequest(SoapBuilderResponseVO soapResponse) {
		HttpCommunicatorRequestVO vo = new HttpCommunicatorRequestVO();
		vo.setLocationURI(soapResponse.getLocationURI());
		vo.setMensagem(soapResponse.getMensagem());
		return vo;
	}
	
	private SoapBuilderRequestVO parseNFeEntradaResponseToSoapBuilderRequest(CTeEntradaConnectorImportacaoResponseVO nfeEntradaResponse){
		SoapBuilderRequestVO vo = new SoapBuilderRequestVO();
		vo.setAtributos(nfeEntradaResponse.getDados());
		vo.setOperation(nfeEntradaResponse.getOperacao());
		vo.setPort(nfeEntradaResponse.getPorta());
		vo.setSoapVersion(SoapVersionEnum.valueOf(nfeEntradaResponse.getSoapVersion().name()));
		return vo;
	}
	
	
	public void setConfigProperties(Properties configProperties) {
		this.configProperties = configProperties;
	}
	
	
	public void setHttpCommunicator(HttpCommunicator httpCommunicator) {
		this.httpCommunicator = httpCommunicator;
	}

	private static final String MSG_INFO_OBTER_ENDPOINT = "msgObtendoEndpoint";
	private static final String MSG_SUCESSO_OBTER_ENDPOINT = "msgEndpointObtido";
	private static final String MSG_SUCESSO_CONSUMO_WS = "msgWebServiceCosumido";
	private static final String MSG_RETORNO_CONSUMO_WS = "msgObtendoRetornoDoWebService";
	private static final String MSG_PROCESSO_CONSUMO_WS = "msgConsumindoWebService";
	private static final String MSG_PROCESSO_SOLICITACAO_CONSUMO_WS = "msgSolicitandoImportacao";
	private static final String MSG_INFO_CRIANDO_OBJETO_PARSER_ENVIO = "msgCriandoObjetoDeEnvio";
	
}
