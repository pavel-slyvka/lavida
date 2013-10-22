package com.lavida.swing.groovy.model.collector;

//import com.tangosol.io.pof.PofReader;
//import com.tangosol.io.pof.PofWriter;
//import com.tangosol.io.pof.PortableObject;

import javax.persistence.*;
//import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Source implements Serializable
//        , PortableObject
{

    @Id
    @GeneratedValue
    private Long id;

    private String url;
    private Class<?> clazz;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "SOURCE_FIELDS_MAPPING",
            joinColumns = @JoinColumn(name = "SOURCE_ID"),
            inverseJoinColumns = @JoinColumn(name = "RULE_ID"))
    @MapKeyColumn(name = "FIELD_NAME")
    private Map<String, Rule> fieldsMapping = new HashMap<String, Rule>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;

    private Long updatePeriod;

    private boolean enabled;

    public Source() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Map<String, Rule> getFieldsMapping() {
        return fieldsMapping;
    }

    public void setFieldsMapping(Map<String, Rule> fieldsMapping) {
        this.fieldsMapping = fieldsMapping;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getUpdatePeriod() {
        return updatePeriod;
    }

    public void setUpdatePeriod(Long updatePeriod) {
        this.updatePeriod = updatePeriod;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Source source = (Source) o;

        if (clazz != null ? !clazz.equals(source.clazz) : source.clazz != null) return false;
        if (fieldsMapping != null ? !fieldsMapping.equals(source.fieldsMapping) : source.fieldsMapping != null)
            return false;
        if (id != null ? !id.equals(source.id) : source.id != null) return false;
        if (url != null ? !url.equals(source.url) : source.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (clazz != null ? clazz.hashCode() : 0);
        result = 31 * result + (fieldsMapping != null ? fieldsMapping.hashCode() : 0);
        return result;
    }

/*
    public void readExternal(PofReader pofReader) throws IOException {
        setId(pofReader.readLong(0));
        setUrl(pofReader.readString(1));
        setClazz((Class) pofReader.readObject(2));
        setFieldsMapping(pofReader.readMap(3, new HashMap<String, Rule>()));
        setCreateDate(pofReader.readDate(4));
        setLastUpdate(pofReader.readDate(5));
        setUpdatePeriod(pofReader.readLong(6));
        setEnabled(pofReader.readBoolean(7));
    }

    public void writeExternal(PofWriter pofWriter) throws IOException {
        pofWriter.writeLong(0, getId());
        pofWriter.writeString(1, getUrl());
        pofWriter.writeObject(2, getClazz());
        pofWriter.writeMap(3, getFieldsMapping());
        pofWriter.writeDate(4, getCreateDate());
        pofWriter.writeDate(5, getLastUpdate());
        pofWriter.writeLong(6, getUpdatePeriod());
        pofWriter.writeBoolean(7, isEnabled());
    }
*/

    @Override
    public String toString() {
        return "Source{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", clazz=" + clazz +
                ", fieldsMapping=" + fieldsMapping +
                '}';
    }

}
