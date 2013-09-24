package com.lavida.swing.preferences;

import javax.xml.bind.annotation.*;

/**
 * The class for a custom preset of {@link UsersSettings}.
 * Created: 15:21 18.09.13
 *
 * @author Ruslan
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "presetType", namespace = "http://www.xml.lavida.com/schema/usersSettings.com")
@XmlRootElement
public class PresetSettings {

    @XmlAttribute
    private String name;

    @XmlElement
    private TableSettings notSoldArticlesTableSettings;

    @XmlElement
    private TableSettings soldArticlesTableSettings;

    @XmlElement
    private TableSettings addNewArticlesTableSettings;

    @XmlElement
    private TableSettings allDiscountCardsTableSettings;

    @XmlElement
    private TableSettings addNewDiscountCardsTableSettings;

    public PresetSettings() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TableSettings getNotSoldArticlesTableSettings() {
        return notSoldArticlesTableSettings;
    }

    public void setNotSoldArticlesTableSettings(TableSettings notSoldArticlesTableSettings) {
        this.notSoldArticlesTableSettings = notSoldArticlesTableSettings;
    }

    public TableSettings getSoldArticlesTableSettings() {
        return soldArticlesTableSettings;
    }

    public void setSoldArticlesTableSettings(TableSettings soldArticlesTableSettings) {
        this.soldArticlesTableSettings = soldArticlesTableSettings;
    }

    public TableSettings getAddNewArticlesTableSettings() {
        return addNewArticlesTableSettings;
    }

    public void setAddNewArticlesTableSettings(TableSettings addNewArticlesTableSettings) {
        this.addNewArticlesTableSettings = addNewArticlesTableSettings;
    }

    public TableSettings getAllDiscountCardsTableSettings() {
        return allDiscountCardsTableSettings;
    }

    public void setAllDiscountCardsTableSettings(TableSettings allDiscountCardsTableSettings) {
        this.allDiscountCardsTableSettings = allDiscountCardsTableSettings;
    }

    public TableSettings getAddNewDiscountCardsTableSettings() {
        return addNewDiscountCardsTableSettings;
    }

    public void setAddNewDiscountCardsTableSettings(TableSettings addNewDiscountCardsTableSettings) {
        this.addNewDiscountCardsTableSettings = addNewDiscountCardsTableSettings;
    }

    @Override
    public String toString() {
        return "PresetSettings{" +
                "name='" + name + '\'' +
                ", notSoldArticlesTableSettings=" + notSoldArticlesTableSettings +
                ", soldArticlesTableSettings=" + soldArticlesTableSettings +
                ", addNewArticlesTableSettings=" + addNewArticlesTableSettings +
                ", allDiscountCardsTableSettings=" + allDiscountCardsTableSettings +
                ", addNewDiscountCardsTableSettings=" + addNewDiscountCardsTableSettings +
                '}';
    }
}
