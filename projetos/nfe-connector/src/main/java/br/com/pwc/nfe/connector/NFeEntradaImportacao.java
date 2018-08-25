package br.com.pwc.nfe.connector;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import br.com.pwc.nfe.connector.enums.SoapVersionEnum;
import br.com.pwc.nfe.connector.vo.NFeEntradaConnectorImportacaoRequestVO;
import br.com.pwc.nfe.connector.vo.NFeEntradaConnectorImportacaoResponseVO;

/**
 * Serviço de Importação do NFeEntrada
 * 
 * @author Marcelo Coutinho
 *
 */
@Component
public class NFeEntradaImportacao {
	
	public NFeEntradaConnectorImportacaoResponseVO iniciaImportacao(NFeEntradaConnectorImportacaoRequestVO dados) {
		NFeEntradaConnectorImportacaoResponseVO vo = new NFeEntradaConnectorImportacaoResponseVO();
		vo.setPorta(IMPORTACAO_PORT);
		vo.setOperacao(IMPORTACAO_OPERATION);
		vo.setSoapVersion(IMPORTACAO_SOAP_VERSION);
		
		StringBuilder sb = new StringBuilder();
		sb.append(IMPORTACAO_MENSAGEM_INICIO).append(dados.getXml()).append(IMPORTACAO_MENSAGEM_FIM);
		vo.setDados(Arrays.asList(sb.toString()));
		
		return vo;
	}
	
	private static final String IMPORTACAO_PORT = "WSImportacaoNFEntradaPort";
	private static final String IMPORTACAO_OPERATION = "importacaoNF";
	private static final String IMPORTACAO_MENSAGEM_INICIO = "<![CDATA[<importacaoNFeEntradaRequest xmlns=\"http://www.pwc.com.br/nfe\"><xml>";
	private static final String IMPORTACAO_MENSAGEM_FIM = "</xml></importacaoNFeEntradaRequest>]]>";
	private static final SoapVersionEnum IMPORTACAO_SOAP_VERSION = SoapVersionEnum.SOAP11;
	
}
