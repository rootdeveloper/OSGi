package br.com.pwc.webservice.communicator.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.stereotype.Component;

import br.com.pwc.webservice.communicator.http.vo.HttpCommunicatorRequestVO;

@Component
public class HttpCommunicator {
	
	public String comunica(HttpCommunicatorRequestVO vo) throws HttpCommunicatorException {
		String locationURI = vo.getLocationURI();
		String soapAction = vo.getSoapAction();
		String mensagem = vo.getMensagem();
		
		PostMethod post = new PostMethod(locationURI);  
        post.setRequestEntity(new InputStreamRequestEntity(new ByteArrayInputStream(mensagem.getBytes()), mensagem.length()));  
        post.setRequestHeader("Content-type", "text/xml; charset=utf-8");  
        post.setRequestHeader("SOAPAction", soapAction == null ? "" : soapAction);
		HttpClient httpclient = new HttpClient();  
        String xmlRetorno = "";  
        try {  
            httpclient.executeMethod(post);
            InputStream stream = post.getResponseBodyAsStream();
            byte[] bytearray = streamToByteArray(stream);
            xmlRetorno = new String(bytearray);  
            return xmlRetorno;
            
        } catch (Exception e) {  
        	throw new HttpCommunicatorException(e);
            
        } finally {  
            post.releaseConnection();  
        }  
	}
	
	private byte[] streamToByteArray(InputStream stream) throws IOException {
        boolean checkLength = false;
        int length = Integer.MAX_VALUE;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int nextValue = stream.read();
        if (checkLength) length--;
        while (-1 != nextValue && length >= 0) {
            byteStream.write(nextValue);
            nextValue = stream.read();
            if (checkLength) length--;
        }
        return byteStream.toByteArray();
	}

}
