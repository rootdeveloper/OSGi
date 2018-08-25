package br.com.pwc.webservice.communicator.http.vo;

import org.springframework.stereotype.Component;

@Component
public class HttpCommunicatorRequestVO {
	
	private String locationURI;
	private String mensagem;
	private String soapAction;

	public void setLocationURI(String locationURI) {
		this.locationURI = locationURI;
	}

	public String getLocationURI() {
		return locationURI;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setSoapAction(String soapAction) {
		this.soapAction = soapAction;
	}

	public String getSoapAction() {
		return soapAction;
	}

}
