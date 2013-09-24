package com.lavida.swing.preferences;

import org.springframework.stereotype.Repository;

import javax.swing.*;

/**
 * The holder for the {@link UsersSettings} object.
 * Created by Ruslan on 19.09.13.
 */
@Repository
public class UsersSettingsHolder {
    private UsersSettings usersSettings;

    private String login;

    private String presetName;

    private JTable notSoldArticlesTable;

    private JTable soldArticlesTable;

    private JTable addNewArticlesTable;

    private JTable allDiscountCardsTable;

    private JTable addNewDiscountCardsTable;

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

    public String getPresetName() {
        return presetName;
    }

    public void setPresetName(String presetName) {
        this.presetName = presetName;
    }

    public JTable getNotSoldArticlesTable() {
        return notSoldArticlesTable;
    }

    public void setNotSoldArticlesTable(JTable notSoldArticlesTable) {
        this.notSoldArticlesTable = notSoldArticlesTable;
    }

    public JTable getSoldArticlesTable() {
        return soldArticlesTable;
    }

    public void setSoldArticlesTable(JTable soldArticlesTable) {
        this.soldArticlesTable = soldArticlesTable;
    }

    public JTable getAddNewArticlesTable() {
        return addNewArticlesTable;
    }

    public void setAddNewArticlesTable(JTable addNewArticlesTable) {
        this.addNewArticlesTable = addNewArticlesTable;
    }

    public JTable getAllDiscountCardsTable() {
        return allDiscountCardsTable;
    }

    public void setAllDiscountCardsTable(JTable allDiscountCardsTable) {
        this.allDiscountCardsTable = allDiscountCardsTable;
    }

    public JTable getAddNewDiscountCardsTable() {
        return addNewDiscountCardsTable;
    }

    public void setAddNewDiscountCardsTable(JTable addNewDiscountCardsTable) {
        this.addNewDiscountCardsTable = addNewDiscountCardsTable;
    }
}
