package br.com.pwc.cte.connector.vo;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.pwc.cte.connector.enums.SoapVersionEnum;

@Component
public class CTeEntradaConnectorImportacaoResponseVO {
	
	private List<String> dados;
	private String porta;
	private String operacao;
	private SoapVersionEnum soapVersion;
	
	public List<String> getDados() {
		return dados;
	}
	public void setDados(List<String> dados) {
		this.dados = dados;
	}
	public String getPorta() {
		return porta;
	}
	public void setPorta(String porta) {
		this.porta = porta;
	}
	public String getOperacao() {
		return operacao;
	}
	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	public SoapVersionEnum getSoapVersion() {
		return soapVersion;
	}
	public void setSoapVersion(SoapVersionEnum soapVersion) {
		this.soapVersion = soapVersion;
	}
	
	
}

