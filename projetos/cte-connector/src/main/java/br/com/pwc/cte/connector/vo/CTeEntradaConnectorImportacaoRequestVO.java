package br.com.pwc.cte.connector.vo;

import org.springframework.stereotype.Component;

@Component
public class CTeEntradaConnectorImportacaoRequestVO {
	
	private String xml;
	
	public String getXml() {
		return xml;
	}
	
	public void setXml(String xml) {
		this.xml = xml;
	}

}
