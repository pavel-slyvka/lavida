package com.lavida.service;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ViewColumn
 * <p/>
 * Created: 23:35 15.08.13
 *
 * @author Pavel
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewColumn {

    String titleKey() default "";

    boolean show() default true;

    String datePattern() default "dd.MM.yyyy";

    String forbiddenRoles() default "";
}
