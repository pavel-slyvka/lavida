package com.lavida.job;

import com.lavida.service.settings.SettingsService;
import org.springframework.context.MessageSource;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * Created by Pavel on 18.08.13.
 */
public abstract class AbstractJob {

    @Resource
    protected MessageSource messageSource;

    @Resource
    protected SettingsService settingsService;

    protected Locale locale = new Locale.Builder().setLanguage("ru").setRegion("RU").setScript("Cyrl").build();
}
