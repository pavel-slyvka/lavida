package com.lavida.service.entity;

import javax.persistence.*;

/**
 * SettingsJdo
 * <p/>
 * Created: 15:29 11.08.13
 *
 * @author Pavel
 */
@Entity
@Table(name = "settings")
public class SettingsJdo {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "type")
    private String key;

    @Column(name = "value")
    private String value;

    public SettingsJdo() {
    }

    public SettingsJdo(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SettingsJdo{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
