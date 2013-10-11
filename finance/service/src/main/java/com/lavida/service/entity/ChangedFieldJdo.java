package com.lavida.service.entity;

import com.lavida.service.FilterColumn;
import com.lavida.service.FilterType;
import com.lavida.service.ViewColumn;

import javax.persistence.*;
import java.util.Date;

/**
 * The ChangedFieldJdo is a class that brings an information about article changed fields.
 * <p/>
 * Created: 27.09.13 11:07.
 *
 * @author Ruslan.
 */
@Entity
@Table(name = "field_changes")
@NamedQueries({
        @NamedQuery(name = ChangedFieldJdo.FIND_REFRESHED, query = "select a from ChangedFieldJdo a where a.refreshOperationType is not null"),
        @NamedQuery(name = ChangedFieldJdo.FIND_POSTPONED, query = "select a from ChangedFieldJdo a where a.postponedDate is not null")
})
public class ChangedFieldJdo {
    public static final String FIND_REFRESHED = "ChangedFieldJdo.findRefreshed";
    public static final String FIND_POSTPONED = "ChangedFieldJdo.findPostponed";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Transient
    @ViewColumn(titleKey = "dialog.changed.field.article.table.selected.title", columnWidth = 20)
    @FilterColumn(type = FilterType.BOOLEAN_CHECKBOX, checkBoxesNumber = 1, orderForRefreshed = 1, orderForPostponed = 1,
            checkBoxesText = {"mainForm.label.search.checkBox.selected.text"}, checkBoxesAction = "mainForm.label.search.checkBox.selected.action")
    private boolean selected;

    @ViewColumn(titleKey = "dialog.changed.field.article.table.operationDate.title", columnWidth = 125,
            datePattern = "dd.MM.yyyy HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationDate;

    @ViewColumn(titleKey = "dialog.changed.field.article.table.objectType.title", columnWidth = 125)
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

    private int objectId;

    @ViewColumn(titleKey = "dialog.changed.field.article.table.сodeNumber.title", columnWidth = 75)
    @FilterColumn(labelKey = "mainForm.label.search.by.code", orderForRefreshed = 2, orderForPostponed = 2)
    private String code;

    @ViewColumn(titleKey = "dialog.changed.field.article.table.size.title", columnWidth = 65)
    @FilterColumn(type = FilterType.COMBOBOX, labelKey = "mainForm.label.search.by.size", orderForRefreshed = 3,
    orderForPostponed = 3)
    private String size;

    @ViewColumn(titleKey = "dialog.changed.field.article.table.fieldName.title", columnWidth = 110)
    private String fieldName;

    @ViewColumn(titleKey = "dialog.changed.field.article.table.oldValue.title" , columnWidth = 200)
    private String oldValue;

    @ViewColumn(titleKey = "dialog.changed.field.article.table.newValue.title", columnWidth = 200)
    private String newValue;

    @ViewColumn(titleKey = "dialog.changed.field.article.table.refreshOperationType.title", columnWidth = 100)
    @FilterColumn(labelKey = "dialog.changed.field.article.filter.refreshOperationType.title", type = FilterType.CHECKBOXES,
            orderForRefreshed = 4, checkBoxesText = {"dialog.changed.field.article.filter.checkBox.update",
            "dialog.changed.field.article.filter.checkBox.save", "dialog.changed.field.article.filter.checkBox.delete"},
            checkBoxesNumber = 3, showForPostponed = false)
    @Enumerated(EnumType.STRING)
    private RefreshOperationType refreshOperationType;


//    @ViewColumn(titleKey = "dialog.changed.field.article.table.postponedDate.title", datePattern = "dd.MM.yyyy HH:mm:ss", columnWidth = 150)
    @Temporal(TemporalType.TIMESTAMP)
    private Date postponedDate;

    public ChangedFieldJdo() {
    }

    public ChangedFieldJdo(Date operationDate, ObjectType objectType, int objectId, String code, String size, String fieldName,
                           String oldValue, String newValue, RefreshOperationType refreshOperationType, Date postponedDate) {
        this.operationDate = operationDate;
        this.objectType = objectType;
        this.objectId = objectId;
        this.code = code;
        this.size = size;
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.refreshOperationType = refreshOperationType;
        this.postponedDate = postponedDate;
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

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
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

    public RefreshOperationType getRefreshOperationType() {
        return refreshOperationType;
    }

    public void setRefreshOperationType(RefreshOperationType refreshOperationType) {
        this.refreshOperationType = refreshOperationType;
    }

    public Date getPostponedDate() {
        return postponedDate;
    }

    public void setPostponedDate(Date postponedDate) {
        this.postponedDate = postponedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChangedFieldJdo that = (ChangedFieldJdo) o;

        if (objectId != that.objectId) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (fieldName != null ? !fieldName.equals(that.fieldName) : that.fieldName != null) return false;
        if (newValue != null ? !newValue.equals(that.newValue) : that.newValue != null) return false;
        if (objectType != that.objectType) return false;
        if (oldValue != null ? !oldValue.equals(that.oldValue) : that.oldValue != null) return false;
        if (operationDate != null ? !operationDate.equals(that.operationDate) : that.operationDate != null)
            return false;
        if (postponedDate != null ? !postponedDate.equals(that.postponedDate) : that.postponedDate != null)
            return false;
        if (refreshOperationType != that.refreshOperationType) return false;
        if (size != null ? !size.equals(that.size) : that.size != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = operationDate != null ? operationDate.hashCode() : 0;
        result = 31 * result + (objectType != null ? objectType.hashCode() : 0);
        result = 31 * result + objectId;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (fieldName != null ? fieldName.hashCode() : 0);
        result = 31 * result + (oldValue != null ? oldValue.hashCode() : 0);
        result = 31 * result + (newValue != null ? newValue.hashCode() : 0);
        result = 31 * result + (refreshOperationType != null ? refreshOperationType.hashCode() : 0);
        result = 31 * result + (postponedDate != null ? postponedDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChangedFieldJdo{" +
                "id=" + id +
                ", selected=" + selected +
                ", operationDate=" + operationDate +
                ", objectType=" + objectType +
                ", objectId=" + objectId +
                ", code='" + code + '\'' +
                ", size='" + size + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                ", refreshOperationType=" + refreshOperationType +
                ", postponedDate=" + postponedDate +
                '}';
    }

    // The type of the refresh operation.
    public enum RefreshOperationType {
        SAVED, DELETED, UPDATED
    }

    // the type of object being changed.
    public enum ObjectType {
        ARTICLE, DISCOUNT_CARD
    }
}
