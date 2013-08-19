package com.lavida.service.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created: 18:30 19.08.13
 * The TagJdo is a representation of financial tags for the type of selling the products ( cash, terminal, privatPlus etc.)
 * @author Ruslan
 */
@Entity
@Table(name = "tags")
public class TagJdo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String uniqueName;

    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    private boolean enable;

    public TagJdo() {
    }

    public TagJdo(String uniqueName, String title, Date createDate, boolean enable) {
        this.uniqueName = uniqueName;
        this.title = title;
        this.createDate = createDate;
        this.enable = enable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "TagJdo{" +
                "id=" + id +
                ", uniqueName='" + uniqueName + '\'' +
                ", title='" + title + '\'' +
                ", createDate=" + createDate +
                ", enable=" + enable +
                '}';
    }
}
