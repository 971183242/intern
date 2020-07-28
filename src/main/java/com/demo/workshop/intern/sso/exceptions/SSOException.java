package com.demo.workshop.intern.sso.exceptions;

/**
 * @author HUISE
 *
 * The SSO exception class.
 */
public class SSOException extends Exception {

    private static final long serialVersionUID = 1029208327114967616L;

    /**
     * Constructs a new SSO exception with <code>null</code> as its detail
     * message.
     * @since SFP.3.0.1
     */
    public SSOException() {
        super();
    }

    /**
     * Constructs a new SSO exception with the specified detail message.
     *
     * @param message
     *            the detail message.
     * @since SFP.3.0.1
     */
    public SSOException(String message) {
        super(message);
    }

    /**
     * Constructs a new runtime exception with the specified detail message and
     * cause.
     *
     * @param message
     *            the detail message.
     * @param cause
     *            the cause.
     * @since SFP.3.0.1
     */
    public SSOException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new runtime exception with the specified cause and a detail
     * message.
     *
     * @param cause
     *            the cause.
     * @since SFP.3.0.1
     */
    public SSOException(Throwable cause) {
        super(cause);
    }

}
