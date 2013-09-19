package com.lavida.service.settings.user;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The class for all users' settings of all swing components.
 * Created: 15:20 18.09.13
 *
 * @author Ruslan
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "usersSettingsType", namespace = "http://www.xml.lavida.com/schema/usersSettings.com")
@XmlRootElement
public class UsersSettings {

    @XmlElement
    private PresetSettings defaultPresetSettings;

    @XmlElement
//    @XmlList
    private List<UserSettings> userSettingsList;

    public UsersSettings() {
    }

    public PresetSettings getDefaultPresetSettings() {
        return defaultPresetSettings;
    }

    public void setDefaultPresetSettings(PresetSettings defaultPresetSettings) {
        this.defaultPresetSettings = defaultPresetSettings;
    }

    public List<UserSettings> getUserSettingsList() {
        if (userSettingsList == null) {
            userSettingsList = new ArrayList<>();
        }
        return userSettingsList;
    }

    public void setUserSettingsList(List<UserSettings> userSettingsList) {
        this.userSettingsList = userSettingsList;
    }

    @Override
    public String toString() {
        return "UsersSettings{" +
                "defaultPresetSettings=" + defaultPresetSettings +
                ", userSettingsList=" + userSettingsList +
                '}';
    }
}
