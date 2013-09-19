package com.lavida.service.preset.settings;

import java.util.Map;

/**
 * The settings for the {@link com.lavida.swing.form.component.ArticleTableComponent}.
 * Created: 15:25 18.09.13
 *
 * @author Ruslan
 */
public class ArticlesTableSettings {

    private Map<String, Integer> columnsHeadersAndIndices;

    public Map<String, Integer> getColumnsHeadersAndIndices() {
        return columnsHeadersAndIndices;
    }

    public void setColumnsHeadersAndIndices(Map<String, Integer> columnsHeadersAndIndices) {
        this.columnsHeadersAndIndices = columnsHeadersAndIndices;
    }

    public ArticlesTableSettings() {
    }

    @Override
    public String toString() {
        return "ArticlesTableSettings{" +
                "columnsHeadersAndIndices=" + columnsHeadersAndIndices +
                '}';
    }
}
