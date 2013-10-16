package com.lavida.service.remote.google;

/**
 * The LavidaGoogleException
 * <p/>
 * Created: 16.10.13 9:29.
 *
 * @author Ruslan.
 */
public class LavidaGoogleException extends Exception {
    public static final int UNAUTHORIZED = 301;
    public static final int NOT_FOUND_SPREADSHEET_LIST = 302;
    public static final int NOT_FOUND_SPREADSHEET = 303;
    public static final int EMPTY_SPREADSHEET_NAME = 304;
    public static final int NOT_FOUND_WORKSHEET = 305;
    public static final int NOT_FOUND_COLUMN_MAPPING= 306;

    private int errorCode;
//
    public LavidaGoogleException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public LavidaGoogleException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public LavidaGoogleException(String message) {
        super(message);
    }
}
