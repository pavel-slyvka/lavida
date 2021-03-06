package com.lavida.service;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

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

    boolean showForPostponed() default true;

    boolean showForRefreshed() default true;

    int orderForSell() default 0;

    int orderForSold() default 0;

    int orderForAddNew() default 0;

    int orderForAllDiscountCards() default 0;

    int orderForRefreshed() default 0;

    int orderForPostponed() default 0;

    int checkBoxesNumber() default 0;

    String[] checkBoxesText() default {};

    String[] checkBoxesAction() default {};

}
