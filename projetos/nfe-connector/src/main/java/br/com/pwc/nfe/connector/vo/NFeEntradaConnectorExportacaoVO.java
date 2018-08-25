package br.com.pwc.nfe.connector.vo;

import java.util.Date;

public class NFeEntradaConnectorExportacaoVO {
	
	private String wsdl;
	private Date dataInicial;
	private Date dataFinal;
	private Integer quantidadeInicial;
	private Integer quantidadeFinal;
	private String cnpjDestinatario;
	private String ieDestinatario;
	
	public String getWsdl() {
		return wsdl;
	}
	public void setWsdl(String wsdl) {
		this.wsdl = wsdl;
	}
	public Date getDataInicial() {
		return dataInicial;
	}
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}
	public Date getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	public Integer getQuantidadeInicial() {
		return quantidadeInicial;
	}
	public void setQuantidadeInicial(Integer quantidadeInicial) {
		this.quantidadeInicial = quantidadeInicial;
	}
	public Integer getQuantidadeFinal() {
		return quantidadeFinal;
	}
	public void setQuantidadeFinal(Integer quantidadeFinal) {
		this.quantidadeFinal = quantidadeFinal;
	}
	public String getCnpjDestinatario() {
		return cnpjDestinatario;
	}
	public void setCnpjDestinatario(String cnpjDestinatario) {
		this.cnpjDestinatario = cnpjDestinatario;
	}
	public String getIeDestinatario() {
		return ieDestinatario;
	}
	public void setIeDestinatario(String ieDestinatario) {
		this.ieDestinatario = ieDestinatario;
	}
	
}
