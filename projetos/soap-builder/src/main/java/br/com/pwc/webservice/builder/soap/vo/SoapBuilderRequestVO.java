package br.com.pwc.webservice.builder.soap.vo;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.pwc.webservice.builder.soap.enums.SoapVersionEnum;

@Component
public class SoapBuilderRequestVO {
	
	private String wsdl;
	private String operation;
	private String port;
	private SoapVersionEnum soapVersion;
	private List<String> atributos;
	
	public String getWsdl() {
		return wsdl;
	}
	
	public void setWsdl(String wsdl) {
		this.wsdl = wsdl;
	}
	
	public String getPort() {
		return port;
	}
	
	public void setPort(String port) {
		this.port = port;
	}
	
	public List<String> getAtributos() {
		return atributos;
	}
	
	public void setAtributos(List<String> atributos) {
		this.atributos = atributos;
	}
	
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	public String getOperation() {
		return operation;
	}
	
	public void setSoapVersion(SoapVersionEnum soapVersion) {
		this.soapVersion = soapVersion;
	}

	public SoapVersionEnum getSoapVersion() {
		return soapVersion;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("WSDL : ");
		sb.append(wsdl);
		sb.append(", PORT : ");
		sb.append(port);
		sb.append(", OPERATION : ");
		sb.append(operation);
		sb.append(", SOAP VERSION : ");
		sb.append(soapVersion);
		sb.append(", ATRIBUTOS : ");
		sb.append(atributos);
		return sb.toString();
	}
	
}
