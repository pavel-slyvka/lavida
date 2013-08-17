package com.lavida.service.settings;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * SettingsHolder
 * <p/>
 * Created: 13:18 12.08.13
 *
 * @author Pavel
 */
@Repository
public class SettingsHolder {

    @Resource
    private SettingsService settingsService;

    private Settings settings;

    public Settings getSettings() {
        if (settings == null) {
            settings = settingsService.getSettings();
        }
        return settings;
    }
}
