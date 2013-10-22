package com.lavida.swing.groovy.model;

//import com.tangosol.io.pof.PofReader;
//import com.tangosol.io.pof.PofWriter;
//import com.tangosol.io.pof.PortableObject;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Comment implements Serializable
//        , PortableObject
{

	private static final long serialVersionUID = 3842279515371423606L;
	
	@Id
    @GeneratedValue
    private long id;
    private long  version;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    private String owner;
    private String text;
    private String date;
    private String link;
    private int rating;

    public Comment() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (id != comment.id) return false;
        if (rating != comment.rating) return false;
        if (version != comment.version) return false;
        if (date != null ? !date.equals(comment.date) : comment.date != null) return false;
        if (link != null ? !link.equals(comment.link) : comment.link != null) return false;
        if (owner != null ? !owner.equals(comment.owner) : comment.owner != null) return false;
        if (text != null ? !text.equals(comment.text) : comment.text != null) return false;
        if (updateTime != null ? !updateTime.equals(comment.updateTime) : comment.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (version ^ (version >>> 32));
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + rating;
        return result;
    }

/*
    public void readExternal(PofReader pofReader) throws IOException {
        setId(pofReader.readLong(0));
        setVersion(pofReader.readLong(1));
        setUpdateTime(pofReader.readDate(2));
        setOwner(pofReader.readString(3));
        setText(pofReader.readString(4));
        setDate(pofReader.readString(5));
        setLink(pofReader.readString(6));
        setRating(pofReader.readInt(7));
    }

    public void writeExternal(PofWriter pofWriter) throws IOException {
        pofWriter.writeLong(0, getId());
        pofWriter.writeLong(1, getVersion());
        pofWriter.writeDate(2, getUpdateTime());
        pofWriter.writeString(3, getOwner());
        pofWriter.writeString(4, getText());
        pofWriter.writeString(5, getDate());
        pofWriter.writeString(6, getLink());
        pofWriter.writeInt(7, getRating());
    }
*/

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", version=" + version +
                ", updateTime=" + updateTime +
                ", owner='" + owner + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", link='" + link + '\'' +
                ", rating=" + rating +
                '}';
    }
}


