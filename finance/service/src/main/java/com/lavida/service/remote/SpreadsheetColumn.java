package com.lavida.service.remote;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * SpreadsheetColumn
 * <p/>
 * Created: 20:15 13.08.13
 *
 * @author Pavel
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SpreadsheetColumn {

    String column();

    String datePattern() default "M/d/yyyy";
}
