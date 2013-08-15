package com.lavida.utils;

import java.lang.annotation.Annotation;

/**
 * ReflectionUtils
 * Created: 22:15 15.08.13
 *
 * @author Pavel
 */
public class ReflectionUtils {

    public static <T extends Annotation> T getFieldAnnotation(Class clazz, String fieldName, Class<T> annotation) {
        try {
            java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
            return field.getAnnotation(annotation);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

}
