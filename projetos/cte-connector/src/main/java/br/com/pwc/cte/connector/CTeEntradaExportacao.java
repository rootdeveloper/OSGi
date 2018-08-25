package br.com.pwc.cte.connector;

import br.com.pwc.cte.connector.enums.SoapVersionEnum;
import br.com.pwc.cte.connector.vo.CTeEntradaConnectorExportacaoVO;

/**
 * Serviço de Exportação do NFeEntrada
 * 
 * @author Marcelo Coutinho
 *
 */
public class CTeEntradaExportacao {
	
	
	public String exportar(CTeEntradaConnectorExportacaoVO dados) {
//		SoapBuilderRequestVO vo = new SoapBuilderRequestVO();
//		vo.setWsdl(dados.getWsdl());
//		vo.setPort(EXPORTACAO_PORT);
//		vo.setOperation(EXPORTACAO_OPERATION);
//		vo.setSoapVersion(EXPORTACAO_SOAP_VERSION);
//		vo.setAtributos(Arrays.asList(buildMessage(dados)));
//		SoapBuilderResponseVO mensagem = soapBuilder.construirMensagem(vo);
//		HttpCommunicatorRequestVO httpVO = parseSoapBuilderResponseToHttpCommunicatorRequest(mensagem);
//		httpCommunicator.comunica(httpVO);
		return null;
	}
	
	private String buildMessage(CTeEntradaConnectorExportacaoVO vo) {
		StringBuilder sb = new StringBuilder();
//		sb.append(b);
//		sb.append(b);
//		sb.append(b);
		return sb.toString();
	}
	
	private static final String EXPORTACAO_PORT = "WSExportacaoNFPort";
	private static final String EXPORTACAO_OPERATION = "exportacaoNF";
	private static final String EXPORTACAO_MENSAGEM_INICIO = "<![CDATA[<exportacaoNFTerceiros xmlns=\"http://www.pwc.com.br/nfe\">";
	private static final String EXPORTACAO_MENSAGEM_FIM = "</exportacaoNFTerceiros>]]>";
	private static final SoapVersionEnum EXPORTACAO_SOAP_VERSION = SoapVersionEnum.SOAP11;
	
	private static final String MENSAGEM_DATA_INICIAL = "<dataInicial>§</dataInicial>";
	private static final String MENSAGEM_DATA_FINAL = "<dataFinal>§</dataFinal>";
	private static final String MENSAGEM_QTD_INICIAL = "<qtdInicial>§</qtdInicial>";
	private static final String MENSAGEM_QTD_FINAL = "<qtdFinal>§</qtdFinal>";
	private static final String MENSAGEM_CNPJ = "<cnpjDestinatario>§</cnpjDestinatario>";
	private static final String MENSAGEM_IE = "<ieDestinatario>§</ieDestinatario>";
	
}
