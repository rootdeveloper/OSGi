package br.com.pwc.webservice.builder.soap.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum que representa os tipos de Elements do wsdl
 * 
 * @author Marcelo Coutinho
 */
public enum ElementTypeEnum {
	SIMPLE_ELEMENT("element"),
	COMPLEX_ELEMENT("complexElement");

	private ElementTypeEnum(String tipo) {
		this.tipo = tipo;
	}

	private static Map<String, ElementTypeEnum> mapa = new HashMap<String, ElementTypeEnum>();

	static {
		for (ElementTypeEnum tipo : EnumSet.allOf(ElementTypeEnum.class)) {
			mapa.put(tipo.tipo, tipo);
		}
	}

	/**
	 * Recupera um Enum pelo seu tipo
	 * @param tipo do enum
	 * @return {@link ElementTypeEnum} Enum que representa esse tipo
	 */
	public ElementTypeEnum lookupByTipo(String tipo) {
		return mapa.get(tipo);
	}

	private String tipo;
}