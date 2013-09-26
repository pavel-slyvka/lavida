package com.lavida.swing.preferences;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * The EditorsSettings
 * <p/>
 * Created: 24.09.13 16:07.
 *
 * @author Ruslan.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "editorsSettingsType", namespace = "http://www.xml.lavida.com/schema/usersSettings.com")
@XmlRootElement
public class EditorsSettings {
    public static final String ARTICLES_TABLE = "articlesTableEditor";
    public static final String DISCOUNT_CARDS_TABLE = "discountCardsTableEditor";

    @XmlElement
    private List<TableEditorSettings> tableEditor;


    public EditorsSettings() {
    }

    public List<TableEditorSettings> getTableEditor() {
        return tableEditor;
    }

    public void setTableEditor(List<TableEditorSettings> tableEditor) {
        this.tableEditor = tableEditor;
    }
}
