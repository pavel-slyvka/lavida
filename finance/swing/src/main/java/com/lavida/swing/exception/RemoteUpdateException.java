package com.lavida.swing.exception;

/**
 * The RemoteUpdateException occurs when updating objects to remote database
 * <p/>
 * Created: 11.10.13 18:34.
 *
 * @author Ruslan.
 */
public class RemoteUpdateException extends Exception {

    public RemoteUpdateException(String message) {
        super(message);
    }

    public RemoteUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteUpdateException(Throwable cause) {
        super(cause);
    }
}
