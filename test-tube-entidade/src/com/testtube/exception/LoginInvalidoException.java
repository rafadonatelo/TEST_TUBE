package com.testtube.exception;

/**
 * Classe para tratamento de exceção de data invalida.
 * 
 */
public class LoginInvalidoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construtor default.
	 */
	public LoginInvalidoException() {
		super();
	}

	/**
	 * Construtor com uma mensagem e o problema.
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public LoginInvalidoException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Construtor apenas com uma mensagem.
	 * 
	 * @param arg0
	 */
	public LoginInvalidoException(String arg0) {
		super(arg0);
	}

	/**
	 * Construtor apenas com o problema.
	 * 
	 * @param arg0
	 */
	public LoginInvalidoException(Throwable arg0) {
		super(arg0);
	}

}
