package br.com.pwc.nfe.integracao.mail.config;

/**
 * Classe de representação de uma conta.
 * 
 * @author sergio.moreira
 *
 */
public class Account {

	private String server;
	private String user;
	private String passwd;
	private int port;
	private boolean ssl;
	private Long timeOut;
	private String protocolo;
	private boolean debugAuthenticador;
	private boolean debugMail;
	private String socketFactorySSL;
	private String socketFacotorySSLPort;
	
	public Account() {

	}
	
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public boolean isSsl() {
		return ssl;
	}
	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

	public Long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(Long timeOut) {
		this.timeOut = timeOut;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public boolean isDebugAuthenticador() {
		return debugAuthenticador;
	}

	public void setDebugAuthenticador(boolean debugAuthenticador) {
		this.debugAuthenticador = debugAuthenticador;
	}

	public boolean isDebugMail() {
		return debugMail;
	}

	public void setDebugMail(boolean debugMail) {
		this.debugMail = debugMail;
	}

	public String getSocketFactorySSL() {
		return socketFactorySSL;
	}

	public void setSocketFactorySSL(String socketFactorySSL) {
		this.socketFactorySSL = socketFactorySSL;
	}

	public String getSocketFacotorySSLPort() {
		return socketFacotorySSLPort;
	}

	public void setSocketFacotorySSLPort(String socketFacotorySSLPort) {
		this.socketFacotorySSLPort = socketFacotorySSLPort;
	}
	
}
