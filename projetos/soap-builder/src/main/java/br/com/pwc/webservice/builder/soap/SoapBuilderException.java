package br.com.pwc.webservice.builder.soap;

public class SoapBuilderException extends Exception{
	
	private static final long serialVersionUID = 3343850420024560833L;

	/**
	 * Construtor
	 */
	public SoapBuilderException() {
		super();
	}

	/**
	 * Construtor passando como parametro a mensagem de erro e o exceção que gerou o erro
	 *
	 * @param message informando o erro
	 * @param cause Exceção que gerou o erro
	 */
	public SoapBuilderException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construtor passando como parametro a mensagem de erro
	 * 
	 * @param message informando o erro
	 */
	public SoapBuilderException(String message) {
		super(message);
	}

	/**
	 * Construtor passando como parametro a exceção que gerou o erro
	 *
	 * @param cause Exceção que gerou o erro
	 */
	public SoapBuilderException(Throwable cause) {
		super(cause);
	}
}
