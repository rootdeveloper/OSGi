package br.com.pwc.nfe.connector.vo;

import org.springframework.stereotype.Component;

@Component
public class NFeEntradaConnectorImportacaoRequestVO {
	
	private String xml;
	
	public String getXml() {
		return xml;
	}
	
	public void setXml(String xml) {
		this.xml = xml;
	}

}
