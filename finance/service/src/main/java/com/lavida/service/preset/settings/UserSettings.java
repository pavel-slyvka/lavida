package com.lavida.service.preset.settings;

import java.util.List;

/**
 * The class for each user's settings of all swing components.
 * Created: 15:20 18.09.13
 *
 * @author Ruslan
 */
public class UserSettings {

    private String userName;

    private List<Preset> presets;

    public UserSettings() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserSettings that = (UserSettings) o;

        if (presets != null ? !presets.equals(that.presets) : that.presets != null) return false;
        if (!userName.equals(that.userName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userName.hashCode();
        result = 31 * result + (presets != null ? presets.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "userName='" + userName + '\'' +
                ", presets=" + presets +
                '}';
    }
}
