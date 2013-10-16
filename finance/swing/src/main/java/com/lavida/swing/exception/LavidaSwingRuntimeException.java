package com.lavida.swing.exception;

import com.lavida.swing.form.AbstractForm;

import java.awt.*;

/**
 * LavidaSwingRuntimeException
 * <p/>
 * Created: 21:53 11.08.13
 *
 * @author Pavel
 */
public class LavidaSwingRuntimeException extends RuntimeException {
    public static final int UNKNOWN_ERROR = 0;
    public static final int GOOGLE_SERVICE_EXCEPTION = 101;
    public static final int GOOGLE_IO_EXCEPTION = 102;
    public static final int DATE_FORMAT_EXCEPTION = 200;
    public static final int PRINTER_EXCEPTION = 201;
    public static final int JAXB_EXCEPTION = 202;
    public static final int IO_EXCEPTION = 203;
    // exactly the same codes as in the LavidaGoogleException - do not change all 300-s!
    public static final int UNAUTHORIZED = 301;
    public static final int NOT_FOUND_SPREADSHEET_LIST = 302;
    public static final int NOT_FOUND_SPREADSHEET = 303;
    public static final int EMPTY_SPREADSHEET_NAME = 304;
    public static final int NOT_FOUND_WORKSHEET = 305;
    public static final int NOT_FOUND_COLUMN_MAPPING= 306;

    private int errorCode;

    public LavidaSwingRuntimeException() {
    }

    public LavidaSwingRuntimeException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
