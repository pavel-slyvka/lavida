package com.lavida.service.settings;

/**
 * Settings
 * <p/>
 * Created: 16:03 11.08.13
 *
 * @author Pavel
 */
public class Settings {

    @SettingsMapping(value = "google.spreadsheet.login", encrypted = true)
    private String remoteUser;

    @SettingsMapping(value = "google.spreadsheet.password", encrypted = true)
    private String remotePass;

    @SettingsMapping(value = "google.spreadsheet.name", encrypted = true)
    private String spreadsheetName;

    @SettingsMapping(value = "google.spreadsheet.worksheet", encrypted = true)
    private int worksheetNumber;

    @SettingsMapping(value = "notification.email.address", encrypted = true)
    private String email;

    @SettingsMapping(value = "notification.email.password", encrypted = true)
    private String emailPass;

    public String getRemoteUser() {
        return remoteUser;
    }

    public void setRemoteUser(String remoteUser) {
        this.remoteUser = remoteUser;
    }

    public String getRemotePass() {
        return remotePass;
    }

    public void setRemotePass(String remotePass) {
        this.remotePass = remotePass;
    }

    public String getSpreadsheetName() {
        return spreadsheetName;
    }

    public void setSpreadsheetName(String spreadsheetName) {
        this.spreadsheetName = spreadsheetName;
    }

    public int getWorksheetNumber() {
        return worksheetNumber;
    }

    public void setWorksheetNumber(int worksheetNumber) {
        this.worksheetNumber = worksheetNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailPass() {
        return emailPass;
    }

    public void setEmailPass(String emailPass) {
        this.emailPass = emailPass;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "remoteUser='" + remoteUser + '\'' +
                ", remotePass='" + remotePass + '\'' +
                ", spreadsheetName='" + spreadsheetName + '\'' +
                ", worksheetNumber=" + worksheetNumber +
                ", email=" + email +
                ", emailPass=" + emailPass +
                '}';
    }
}
