package com.lavida.service.entity;

import com.lavida.service.FilterColumn;
import com.lavida.service.FilterType;
import com.lavida.service.ViewColumn;

import javax.persistence.*;
import java.util.Date;

/**
 * The ArticleChangedFieldJdo is a class that brings an information about article changed fields.
 * <p/>
 * Created: 27.09.13 11:07.
 *
 * @author Ruslan.
 */
@Entity
@Table(name = "article_changes")
@NamedQueries({
//        @NamedQuery(name = ArticleChangedFieldJdo.FIND_BY_CODE, query = "select a from ArticleChangedFieldJdo a where a.code = : code"),
//        @NamedQuery(name = ArticleChangedFieldJdo.FIND_BY_OPERATION_DATE, query = "select a from ArticleChangedFieldJdo a where a.operationDate = : date"),
        @NamedQuery(name = ArticleChangedFieldJdo.FIND_All, query = "select a from ArticleChangedFieldJdo a")
})
public class ArticleChangedFieldJdo {
    //    public static final String FIND_BY_CODE = "ArticleChangedFieldJdo.findByCode";
//    public static final String FIND_BY_OPERATION_DATE = "ArticleChangedFieldJdo.findByOperationDate";
    public static final String FIND_All = "ArticleChangedFieldJdo.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Transient
    @ViewColumn(titleKey = "dialog.changed.field.article.table.selected.title", columnWidth = 20)
    @FilterColumn(type = FilterType.BOOLEAN_CHECKBOX, checkBoxesNumber = 1, orderForArticleChangedField = 1,
            checkBoxesText = {"mainForm.label.search.checkBox.selected.text"}, checkBoxesAction = "mainForm.label.search.checkBox.selected.action")
    private boolean selected;

    @ViewColumn(titleKey = "dialog.changed.field.article.table.operationDate.title", columnWidth = 125,
            datePattern = "dd.MM.yyyy HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationDate;

    @ViewColumn(titleKey = "dialog.changed.field.article.table.сode.title", columnWidth = 75)
    @FilterColumn(labelKey = "mainForm.label.search.by.code", orderForArticleChangedField = 2)
    private String code;

    @ViewColumn(titleKey = "dialog.changed.field.article.table.size.title", columnWidth = 65)
    @FilterColumn(type = FilterType.COMBOBOX, labelKey = "mainForm.label.search.by.size", orderForArticleChangedField = 3)
    private String size;

    @ViewColumn(titleKey = "dialog.changed.field.article.table.fieldName.title", columnWidth = 110)
    private String fieldName;

    @ViewColumn(titleKey = "dialog.changed.field.article.table.oldValue.title" , columnWidth = 200)
    private String oldValue;

    @ViewColumn(titleKey = "dialog.changed.field.article.table.newValue.title", columnWidth = 200)
    private String newValue;

    @ViewColumn(titleKey = "dialog.changed.field.article.table.operationType.title", columnWidth = 100)
    @FilterColumn(labelKey = "dialog.changed.field.article.filter.operationType.title", type = FilterType.CHECKBOXES,
            orderForArticleChangedField = 4, checkBoxesText = {"dialog.changed.field.article.filter.checkBox.update",
            "dialog.changed.field.article.filter.checkBox.save", "dialog.changed.field.article.filter.checkBox.delete"},
            checkBoxesNumber = 3)
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    public ArticleChangedFieldJdo() {
    }

    public ArticleChangedFieldJdo(Date operationDate, String code, String size, String fieldName, String oldValue,
                                  String newValue, OperationType operationType) {
        this.operationDate = operationDate;
        this.code = code;
        this.size = size;
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.operationType = operationType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArticleChangedFieldJdo that = (ArticleChangedFieldJdo) o;

        if (id != that.id) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (fieldName != null ? !fieldName.equals(that.fieldName) : that.fieldName != null) return false;
        if (newValue != null ? !newValue.equals(that.newValue) : that.newValue != null) return false;
        if (oldValue != null ? !oldValue.equals(that.oldValue) : that.oldValue != null) return false;
        if (operationDate != null ? !operationDate.equals(that.operationDate) : that.operationDate != null)
            return false;
        if (operationType != that.operationType) return false;
        if (size != null ? !size.equals(that.size) : that.size != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (operationDate != null ? operationDate.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (fieldName != null ? fieldName.hashCode() : 0);
        result = 31 * result + (oldValue != null ? oldValue.hashCode() : 0);
        result = 31 * result + (newValue != null ? newValue.hashCode() : 0);
        result = 31 * result + (operationType != null ? operationType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ArticleChangedFieldJdo{" +
                "id=" + id +
                ", operationDate=" + operationDate +
                ", code='" + code + '\'' +
                ", size='" + size + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                ", operationType=" + operationType +
                '}';
    }

    // The type of  an article changing operation.
    public enum OperationType {
        SAVED, DELETED, UPDATED
    }
}
