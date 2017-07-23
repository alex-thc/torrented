package com.webapp.exception;

public class InvalidMagnetLinkException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4637346399024563637L;

	public InvalidMagnetLinkException() {
    }

    public InvalidMagnetLinkException(String message) {
        super(message);
    }

    public InvalidMagnetLinkException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMagnetLinkException(Throwable cause) {
        super(cause);
    }

    public InvalidMagnetLinkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
