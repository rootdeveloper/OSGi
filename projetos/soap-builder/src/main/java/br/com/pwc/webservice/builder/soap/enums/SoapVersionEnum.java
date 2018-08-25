package br.com.pwc.webservice.builder.soap.enums;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.soap.SOAPFactory;

/**
 * Enum com os tipos de SOAP 
 * 
 * @author Marcelo Coutinho
 */
public enum SoapVersionEnum {
	
	SOAP11 {
		@Override
		public SOAPFactory getSoapFactory() {
			return OMAbstractFactory.getSOAP11Factory();
		}
	},
	SOAP12 {
		@Override
		public SOAPFactory getSoapFactory() {
			return OMAbstractFactory.getSOAP12Factory();
		}
	};

	/**
	 * Recupera o SOAPFactory específico
	 * 
	 * @return {@link SOAPFactory}
	 */
	public abstract SOAPFactory getSoapFactory();
}
