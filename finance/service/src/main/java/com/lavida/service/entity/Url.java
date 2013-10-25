package com.lavida.service.entity;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 23.10.13
 * Time: 15:16
 * To change this template use File | Settings | File Templates.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Url.FIND_BY_URL, query = "select u from Url u where u.url=:urlString")
})
public class Url {
    public static final String FIND_BY_URL = "Url.findByUrl";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @Column(unique = true)
    private String url;

    private String filePath;

    private boolean processed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Url url1 = (Url) o;

        if (id != url1.id) return false;
        if (processed != url1.processed) return false;
        if (filePath != null ? !filePath.equals(url1.filePath) : url1.filePath != null) return false;
        if (title != null ? !title.equals(url1.title) : url1.title != null) return false;
        if (url != null ? !url.equals(url1.url) : url1.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
        result = 31 * result + (processed ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Url{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", filePath='" + filePath + '\'' +
                ", processed=" + processed +
                '}';
    }
}
