package com.lavida.service;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * FilterColumn
 * <p/>
 * Created: 12:35 17.08.13
 *
 * @author Pavel
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterColumn {

    String labelKey() default "";

    int editSize() default 25;

    FilterType type() default FilterType.FULL_TEXT;

    boolean showForSell() default true;

    boolean showForSold() default true;

    boolean showForAddNew() default true;

    boolean showForAllDiscountCards() default true;

    int orderForSell() default 0;

    int orderForSold() default 0;

    int orderForAddNew() default 0;

    int orderForAllDiscountCards() default 0;
}
