package com.lavida.swing.preferences.user;

import org.springframework.stereotype.Repository;

/**
 * The holder for the {@link com.lavida.swing.preferences.user.UsersSettings} object.
 * Created by Ruslan on 19.09.13.
 */
@Repository
public class UsersSettingsHolder {
    private UsersSettings usersSettings;

    private String login;

    public UsersSettings getUsersSettings() {
        return usersSettings;
    }

    public void setUsersSettings(UsersSettings usersSettings) {
        this.usersSettings = usersSettings;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
