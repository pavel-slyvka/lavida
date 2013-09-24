package com.lavida.swing.preferences;

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

    @XmlAttribute
    private String lastPresetName;

    @XmlElement
    private List<PresetSettings> presetSettingsList;

    public UserSettings() {
    }

    public UserSettings(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<PresetSettings> getPresetSettingsList() {
        if (presetSettingsList == null) {
            presetSettingsList = new ArrayList<>();
        }
        return presetSettingsList;
    }

    public String getLastPresetName() {
        return lastPresetName;
    }

    public void setLastPresetName(String lastPresetName) {
        this.lastPresetName = lastPresetName;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "login='" + login + '\'' +
                ", lastPresetName='" + lastPresetName + '\'' +
                ", presetSettingsList=" + presetSettingsList +
                '}';
    }
}
