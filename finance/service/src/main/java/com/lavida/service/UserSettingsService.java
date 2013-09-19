package com.lavida.service;

import com.lavida.service.settings.user.UserSettings;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * The service for the {@link com.lavida.service.settings.user.UserSettings }.
 * Created: 16:38 18.09.13
 *
 * @author Ruslan
 */
@Service
public class UserSettingsService {

    private File settingsFile;

    public void saveSettings(UserSettings userSettings) {
        // todo
    }

    public UserSettings getSettings() {
        return null;    // todo
    }

    @PostConstruct
    public void init() {
        String filePath = System.getProperty("user.dir");
        settingsFile = new File(filePath);
    }
}
