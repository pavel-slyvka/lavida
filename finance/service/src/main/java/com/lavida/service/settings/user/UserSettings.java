package com.lavida.service.settings.user;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The class for a user's settings.
 * Created by Pavel on 19.09.13.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userSettingsType", namespace = "http://www.xml.lavida.com/schema/usersSettings.com")
@XmlRootElement
public class UserSettings {

    @XmlAttribute
    private String login;

    @XmlElement
    private PresetSettings defaultPresetSettings;

    @XmlElement
//    @XmlList
    private List<PresetSettings> presetSettingsList;

    public UserSettings() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public PresetSettings getDefaultPresetSettings() {
        return defaultPresetSettings;
    }

    public void setDefaultPresetSettings(PresetSettings defaultPresetSettings) {
        this.defaultPresetSettings = defaultPresetSettings;
    }

    public List<PresetSettings> getPresetSettingsList() {
        if (presetSettingsList == null) {
            presetSettingsList = new ArrayList<>();
        }
        return presetSettingsList;
    }

    public void setPresetSettingsList(List<PresetSettings> presetSettingsList) {
        this.presetSettingsList = presetSettingsList;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "login='" + login + '\'' +
                ", defaultPresetSettings=" + defaultPresetSettings +
                ", presetSettingsList=" + presetSettingsList +
                '}';
    }
}
