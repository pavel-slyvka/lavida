package com.lavida.service.settings;

/**
 * Settings
 * <p/>
 * Created: 16:03 11.08.13
 *
 * @author Pavel
 */
public class Settings {

    @SettingsMapping(value = "sheet.refresh.tasks.times", encrypted = false)
    private String sheetRefreshTasksTimes;

    @SettingsMapping(value = "google.spreadsheet.login", encrypted = true)
    private String remoteUser;

    @SettingsMapping(value = "google.spreadsheet.password", encrypted = true)
    private String remotePass;

    @SettingsMapping(value = "google.spreadsheet.name", encrypted = true)
    private String spreadsheetName;

    @SettingsMapping(value = "google.spreadsheet.worksheet", encrypted = true)
    private int worksheetNumber;

    @SettingsMapping(value = "google.discount.spreadsheet.name", encrypted = true)
    private String discountSpreadsheetName;

    @SettingsMapping(value = "google.discount.spreadsheet.worksheet", encrypted = true)
    private int discountWorksheetNumber;

    @SettingsMapping(value = "notification.email.address", encrypted = true)
    private String email;

    @SettingsMapping(value = "notification.email.password", encrypted = true)
    private String emailPass;

    public String getSheetRefreshTasksTimes() {
        return sheetRefreshTasksTimes;
    }

    public void setSheetRefreshTasksTimes(String sheetRefreshTasksTimes) {
        this.sheetRefreshTasksTimes = sheetRefreshTasksTimes;
    }

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

    public String getDiscountSpreadsheetName() {
        return discountSpreadsheetName;
    }

    public void setDiscountSpreadsheetName(String discountSpreadsheetName) {
        this.discountSpreadsheetName = discountSpreadsheetName;
    }

    public int getDiscountWorksheetNumber() {
        return discountWorksheetNumber;
    }

    public void setDiscountWorksheetNumber(int discountWorksheetNumber) {
        this.discountWorksheetNumber = discountWorksheetNumber;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "remoteUser='" + remoteUser + '\'' +
                ", remotePass='" + remotePass + '\'' +
                ", spreadsheetName='" + spreadsheetName + '\'' +
                ", worksheetNumber=" + worksheetNumber +
                ", discountSpreadsheetName='" + discountSpreadsheetName + '\'' +
                ", discountWorksheetNumber=" + discountWorksheetNumber +
                ", email='" + email + '\'' +
                ", emailPass='" + emailPass + '\'' +
                '}';
    }
}
