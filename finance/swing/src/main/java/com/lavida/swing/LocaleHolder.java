package com.lavida.swing;

import org.springframework.stereotype.Repository;

import java.util.Locale;

/**
 * LocaleHolder
 * <p/>
 * Created: 22:18 11.08.13
 *
 * @author Pavel
 */
@Repository
public class LocaleHolder {

    private Locale locale = new Locale.Builder().setLanguage("ru").setRegion("RU").setScript("Cyrl").build();

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "LocaleHolder{" +
                "locale=" + locale +
                '}';
    }
}
