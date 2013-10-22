package com.lavida.swing.groovy.model.collector;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Page implements Serializable{

	private static final long serialVersionUID = -1880904195269771644L;

	@Id
    @GeneratedValue
    private Long id;

    private String url;
    private String host;

    @Lob
    private String content;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    public Page() {
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Page [id=" + id + ", url=" + url + ", host=" + host
				+ ", content=" + content + ", time=" + time + "]";
	}

}

