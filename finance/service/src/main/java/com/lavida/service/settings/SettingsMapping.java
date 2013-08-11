package com.lavida.service.settings;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * SettingsMapping
 * <p/>
 * Created: 16:15 11.08.13
 *
 * @author Pavel
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SettingsMapping {
    String value();
    boolean encrypted() default false;
}
