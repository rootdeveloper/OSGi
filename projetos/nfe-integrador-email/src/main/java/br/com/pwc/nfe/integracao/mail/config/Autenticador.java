package br.com.pwc.nfe.integracao.mail.config;

import javax.mail.PasswordAuthentication;

/**
 * Classe responsavel por criar autenticador do email
 * 
 * @author sergio.moreira
 *
 */
public class Autenticador extends javax.mail.Authenticator{

	private String user;
	private String senha;
	
	/**
	 * Construtor que recebe usuario e senha da conta de email a ser autenticada.
	 * @param user - Usuario do email
	 * @param senha - Senha do email
	 */
	public Autenticador(String user, String senha) {
		this.user = user;
		this.senha = senha;
	}
	
	/**
	 * Metodo sobreescrito que implementa a autenticação.
	 */
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, senha);
	}
	
}
