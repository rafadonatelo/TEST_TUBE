package com.testtube.exception;

public class SystemException extends RuntimeException {

    /**
	 * SerialVersionUID.
	 */
    private static final long serialVersionUID = 1L;

    /**
     * Construtor default.
     */
    public SystemException() {
        super();
    }

    /**
     * Construtor com uma mensagem e o problema.
     * 
     * @param arg0
     * @param arg1
     */
    public SystemException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Construtor apenas com uma mensagem.
     * 
     * @param arg0
     */
    public SystemException(String arg0) {
        super(arg0);
    }

    /**
     * Construtor apenas com o problema.
     * 
     * @param arg0
     */
    public SystemException(Throwable arg0) {
        super(arg0);
    }
}
