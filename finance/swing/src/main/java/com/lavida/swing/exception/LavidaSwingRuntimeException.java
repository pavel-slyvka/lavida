package com.lavida.swing.exception;

import com.lavida.swing.form.AbstractForm;

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
    public static final int DATE_FORMAT_EXCEPTION = 201;
    private int errorCode;
    private AbstractForm form;

    public LavidaSwingRuntimeException() {
    }

    public LavidaSwingRuntimeException(int errorCode, AbstractForm form) {
        super();
        this.errorCode = errorCode;
        this.form = form;
    }

    public LavidaSwingRuntimeException(int errorCode, Throwable cause, AbstractForm form) {
        super(cause);
        this.errorCode = errorCode;
        this.form = form;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public AbstractForm getForm() {
        return form;
    }
}
