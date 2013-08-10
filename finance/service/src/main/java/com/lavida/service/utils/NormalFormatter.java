package com.lavida.service.utils;

/**
 * Created: 21:53 10.08.13
 * The NormalFormatter helps to normalize String expressions and to parse them into needed types.
 * @author Ruslan
 */
public class NormalFormatter {
    /**
     *  Normalizes String expression of number and parses it to double value.
     * @param number   String expression of number to be normalized
     * @return
     */
    public static double doubleNormalize(String number) {
        number = number.replaceFirst(",", ".").replaceAll("[^0-9.]", "");
        return Double.parseDouble(number);

    }
}
