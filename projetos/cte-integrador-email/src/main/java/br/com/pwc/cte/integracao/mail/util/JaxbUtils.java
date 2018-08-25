package br.com.pwc.cte.integracao.mail.util;

import java.io.ByteArrayInputStream;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.pwc.cte.integracao.mail.config.IntegradorEmailMessageFactory;


/**
 * 
 * Classe responsavel por transforma xml e objeto.
 * 
 * @author sergio.moreira
 * @author daniel.santos
 *
 */
@Component
public class JaxbUtils {
	
	private static final Logger LOGGER = Logger.getLogger(JaxbUtils.class);
	
	@Autowired
	private IntegradorEmailMessageFactory messageFactory;
	
	private JAXBContext context;
	
	/**
	 * Metodo que transforma um xml em objeto.
	 * @param <T> Classe generica a ser utilizada para unmarshall.
	 * @param t classe generica
	 * @param xml a ser convertido
	 * @return {@link Class<T>}
	 * @throws Exception
	 */
	public <T> Object unmarshall(T t, String xml) throws Exception {
		try {
			LOGGER.info(messageFactory.getMessage(MSG_XML_IN_OBJECT));
			context = JAXBContext.newInstance(t.getClass());
			Unmarshaller unm = context.createUnmarshaller();
			return unm.unmarshal(new ByteArrayInputStream(xml.getBytes()));
		} catch (JAXBException e) {
			LOGGER.error(messageFactory.getMessage(MSG_ERRO_CONVERTER_XML_IN_OBJECT));
			throw new Exception(e);
		}
	}
	
	/**
	 * Metodo que faz marshal do objeto par xml.
	 * @param <T> Classe generica de marshall.
	 * @param t classe generica.
	 * @param writer Writer de retorno que devolve o xml convertido.
	 * @throws Exception Caso houver algum erro.
	 */
	public <T> void marshall(T t, Writer writer) throws Exception {
		try {
			LOGGER.info(messageFactory.getMessage(MSG_OBJECT_IN_XML));
			context = JAXBContext.newInstance(t.getClass());
			Marshaller mashall = context.createMarshaller();
			mashall.marshal(t, writer);
		} catch (JAXBException e) {
			LOGGER.error(messageFactory.getMessage(MSG_ERRO_CONVERTER_OBJECT_IN_XML));
			throw new Exception(e);
		}
	}
	
	private static final String MSG_XML_IN_OBJECT = "msgConvertendoXml";
	private static final String MSG_OBJECT_IN_XML = "msgCOnvertendoObjeto";
	private static final String MSG_ERRO_CONVERTER_XML_IN_OBJECT = "msgErroAoConverterXml";
	private static final String MSG_ERRO_CONVERTER_OBJECT_IN_XML = "msgErroAoConverterObjeto";
}