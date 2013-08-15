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

    String sheetColumn();

    String sheetDatePattern() default "M/d/yyyy";

    String titleKey() default "";

    boolean show() default true;

    String showDatePattern() default "dd.MM.yyyy";

    String forbiddenRoles() default "";
}
