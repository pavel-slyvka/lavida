package com.lavida.swing.preferences;

import javax.xml.bind.annotation.*;

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

    @XmlElement
    private TableEditorSettings articlesTableEditor;

    @XmlElement
    private TableEditorSettings discountCardsTableEditor;

    public EditorsSettings() {
    }

    public TableEditorSettings getArticlesTableEditor() {
        return articlesTableEditor;
    }

    public void setArticlesTableEditor(TableEditorSettings articlesTableEditor) {
        this.articlesTableEditor = articlesTableEditor;
    }

    public TableEditorSettings getDiscountCardsTableEditor() {
        return discountCardsTableEditor;
    }

    public void setDiscountCardsTableEditor(TableEditorSettings discountCardsTableEditor) {
        this.discountCardsTableEditor = discountCardsTableEditor;
    }

    @Override
    public String toString() {
        return "EditorsSettings{" +
                "articlesTableEditor=" + articlesTableEditor +
                ", discountCardsTableEditor=" + discountCardsTableEditor +
                '}';
    }
}
