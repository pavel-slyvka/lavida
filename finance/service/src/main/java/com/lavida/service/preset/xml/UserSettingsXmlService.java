package com.lavida.service.preset.xml;

/**
 * The service for xml mapping of the {@link com.lavida.service.preset.settings.UserSettings }.
 * Created: 16:52 18.09.13
 *
 * @author Ruslan
 */

import com.lavida.service.preset.settings.UserSettings;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;

@Repository
public class UserSettingsXmlService {

    private File settingsFile;

    public void updateSettings(UserSettings userSettings, File file) {

    }

    public UserSettings getSettings(File file) {
        return null;
    }

    public UserSettingsXmlService() {
    }

    @PostConstruct
    public void init() {
        String filePath = System.getProperty("user.dir");
        settingsFile = new File(filePath);
    }

    public File getSettingsFile() {
        return settingsFile;
    }
}
