package com.lavida.service.settings.user;

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
    private TableSettings articlesTableSettings;

    @XmlElement
    private TableSettings discountCardsTableSettings;

    public PresetSettings() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TableSettings getArticlesTableSettings() {
        return articlesTableSettings;
    }

    public void setArticlesTableSettings(TableSettings articlesTableSettings) {
        this.articlesTableSettings = articlesTableSettings;
    }

    public TableSettings getDiscountCardsTableSettings() {
        return discountCardsTableSettings;
    }

    public void setDiscountCardsTableSettings(TableSettings discountCardsTableSettings) {
        this.discountCardsTableSettings = discountCardsTableSettings;
    }

    @Override
    public String toString() {
        return "PresetSettings{" +
                "name='" + name + '\'' +
                ", articlesTableSettings=" + articlesTableSettings +
                ", discountCardsTableSettings=" + discountCardsTableSettings +
                '}';
    }
}
