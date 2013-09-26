package com.lavida.swing.preferences;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

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
    public static final String NOT_SOLD_ARTICLES_TABLE = "notSoldArticlesTableSettings";
    public static final String SOLD_ARTICLES_TABLE = "soldArticlesTableSettings";
    public static final String ADD_NEW_ARTICLES_TABLE = "addNewArticlesTableSettings";
    public static final String ALL_DISCOUNT_CARDS_TABLE = "allDiscountCardsTableSettings";
    public static final String ADD_NEW_DISCOUNT_CARDS_TABLE = "addNewDiscountCardsTableSettings";

    @XmlAttribute
    private String presetName;

    @XmlElement
    private List<TableSettings> tableSettings;

    public PresetSettings() {
    }

    public String getPresetName() {
        return presetName;
    }

    public void setPresetName(String presetName) {
        this.presetName = presetName;
    }

    public List<TableSettings> getTableSettings() {
        return tableSettings;
    }

    public void setTableSettings(List<TableSettings> tableSettings) {
        this.tableSettings = tableSettings;
    }
}
