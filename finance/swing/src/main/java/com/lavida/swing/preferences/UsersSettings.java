package com.lavida.swing.preferences;

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
    private EditorsSettings editorsSettings;

    @XmlElement
    private List<UserSettings> userSettingsList;

    public UsersSettings() {
    }

    public EditorsSettings getEditorsSettings() {
        return editorsSettings;
    }

    public void setEditorsSettings(EditorsSettings editorsSettings) {
        this.editorsSettings = editorsSettings;
    }

    public List<UserSettings> getUserSettingsList() {
        if (userSettingsList == null) {
            userSettingsList = new ArrayList<>();
        }
        return userSettingsList;
    }

    @Override
    public String toString() {
        return "UsersSettings{" +
                "editorsSettings=" + editorsSettings +
                ", userSettingsList=" + userSettingsList +
                '}';
    }
}
