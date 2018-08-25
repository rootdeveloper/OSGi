package br.com.pwc.nfe.integracao.mail.util;

public class IntegradorException extends Exception{
	
	private static final long serialVersionUID = 3343850420024560833L;

	/**
	 * Construtor
	 */
	public IntegradorException() {
		super();
	}

	/**
	 * Construtor passando como parametro a mensagem de erro e o exceção que gerou o erro
	 *
	 * @param message informando o erro
	 * @param cause Exceção que gerou o erro
	 */
	public IntegradorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construtor passando como parametro a mensagem de erro
	 * 
	 * @param message informando o erro
	 */
	public IntegradorException(String message) {
		super(message);
	}

	/**
	 * Construtor passando como parametro a exceção que gerou o erro
	 *
	 * @param cause Exceção que gerou o erro
	 */
	public IntegradorException(Throwable cause) {
		super(cause);
	}
}
