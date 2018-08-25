package br.com.pwc.webservice.builder.soap.vo;

import org.springframework.stereotype.Component;

@Component
public class SoapBuilderResponseVO {
	
	private String locationURI;
	private String mensagem;

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

}
