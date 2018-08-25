package br.com.pwc.cte.connector;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import br.com.pwc.cte.connector.enums.SoapVersionEnum;
import br.com.pwc.cte.connector.vo.CTeEntradaConnectorImportacaoRequestVO;
import br.com.pwc.cte.connector.vo.CTeEntradaConnectorImportacaoResponseVO;

/**
 * Serviço de Importação do CTe
 * 
 * @author Marcelo Coutinho
 *
 */
@Component
public class CTeEntradaImportacao {
	
	public CTeEntradaConnectorImportacaoResponseVO iniciaImportacao(CTeEntradaConnectorImportacaoRequestVO dados) {
		CTeEntradaConnectorImportacaoResponseVO vo = new CTeEntradaConnectorImportacaoResponseVO();
		vo.setPorta(IMPORTACAO_PORT);
		vo.setOperacao(IMPORTACAO_OPERATION);
		vo.setSoapVersion(IMPORTACAO_SOAP_VERSION);
		
		StringBuilder sb = new StringBuilder();
		sb.append(IMPORTACAO_MENSAGEM_INICIO).append(dados.getXml()).append(IMPORTACAO_MENSAGEM_FIM);
		vo.setDados(Arrays.asList(sb.toString()));
		
		return vo;
	}
	
	private static final String IMPORTACAO_PORT = "WSImportacaoCTEntradaPort";
	private static final String IMPORTACAO_OPERATION = "importacaoCT";
	private static final String IMPORTACAO_MENSAGEM_INICIO = "<![CDATA[<importacaoCTeEntradaRequest xmlns=\"http://www.dataprimer.com.br/cte\"><xml>";
	private static final String IMPORTACAO_MENSAGEM_FIM = "</xml></importacaoCTeEntradaRequest>]]>";
	private static final SoapVersionEnum IMPORTACAO_SOAP_VERSION = SoapVersionEnum.SOAP11;
	
}
