package br.com.pwc.nfe.integracao.xml.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.pwc.nfe.integracao.xml.config.IntegradorXmlMessageFactory;

@Component
public class XmlInspector {
	
	private static final Logger LOGGER = Logger.getLogger(XmlInspector.class);
	
	@Autowired
	private IntegradorXmlMessageFactory messageFactory;
	
	/**
	 * Verifica o c�digo de retorno
	 * @param xml
	 * @return
	 */
	public String identificaCodigoDeRetorno(String xml) {
		
		String codigoRetorno = null;
		
		xml = xml.replace("&lt;", "<");
		xml = xml.replace("&gt;", ">");
		xml = xml.replace("&quot;", "\"");
		xml = xml.replace("&amp;", "&");
		xml = xml.replace("<codigoRetorno>", "��");
		xml = xml.replace("</codigoRetorno>", "��");
		if(xml.contains("��")) {
			int inicio = xml.indexOf("��");
			int fim = xml.lastIndexOf("��");
			codigoRetorno = xml.substring(inicio + 2, fim);
		} 
		
		return codigoRetorno;
	}
	
	public String identificaDescricaoDoRetorno(String xml) {
		
		String descricaoRetorno = null;
		
		xml = xml.replace("&lt;", "<");
		xml = xml.replace("&gt;", ">");
		xml = xml.replace("&quot;", "\"");
		xml = xml.replace("&amp;", "&");
		xml = xml.replace("<descricaoRetorno>", "��");
		xml = xml.replace("</descricaoRetorno>", "��");
		if(xml.contains("��")) {
			int inicio = xml.indexOf("��");
			int fim = xml.lastIndexOf("��");
			descricaoRetorno = xml.substring(inicio + 2, fim);
		}
		return descricaoRetorno;
	}
	
	
	
	public boolean isValid(File file) {
		boolean retorno = true;
		LOGGER.info(messageFactory.getMessage(MSG_PROCESS_VALIDED_FILE) + " " + file.getName());
		if(!file.getName().toLowerCase().endsWith(".xml")) {
			LOGGER.error(messageFactory.getMessage(MSG_FILE_IS_NOT_XML));
			retorno = false;
		}
		
		try {
			String xml = FileUtils.readFileToString(file);
			if(StringUtils.isBlank(xml)) {
				retorno = false;
			}
		} catch (IOException e) {
			LOGGER.error(messageFactory.getMessage(MSG_PROCESS_VALIDED_FILE), e);
		}
		return retorno;
	}
	
	private static final String MSG_PROCESS_VALIDED_FILE = "msgVerificandoXml";
	private static final String MSG_FILE_IS_NOT_XML = "msgArquivoNaoXml";
	private static final String MSG_ERROR_PROCESS_VALIDED_FILE = "msgErroAoValidarXml";
}
