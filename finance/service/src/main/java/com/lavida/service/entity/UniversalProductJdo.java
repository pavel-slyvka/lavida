package com.lavida.service.entity;

import javax.persistence.*;

/**
 * Entity for universal product
 * Created by Admin on 28.10.13.
 * @author Ruslan
 */
@Entity
@Table(name = "universal_product")
public class UniversalProductJdo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String className;

    private long objectId;

    private String fieldName;

    private String fieldValue;

    public UniversalProductJdo() {
    }

    public UniversalProductJdo(String className, long objectId, String fieldName, String fieldValue) {
        this.className = className;
        this.objectId = objectId;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UniversalProductJdo that = (UniversalProductJdo) o;

        if (objectId != that.objectId) return false;
        if (className != null ? !className.equals(that.className) : that.className != null) return false;
        if (fieldName != null ? !fieldName.equals(that.fieldName) : that.fieldName != null) return false;
        if (fieldValue != null ? !fieldValue.equals(that.fieldValue) : that.fieldValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = className != null ? className.hashCode() : 0;
        result = 31 * result + (int) (objectId ^ (objectId >>> 32));
        result = 31 * result + (fieldName != null ? fieldName.hashCode() : 0);
        result = 31 * result + (fieldValue != null ? fieldValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UniversalProductJdo{" +
                "id=" + id +
                ", className='" + className + '\'' +
                ", objectId=" + objectId +
                ", fieldName='" + fieldName + '\'' +
                ", fieldValue='" + fieldValue + '\'' +
                '}';
    }
}
