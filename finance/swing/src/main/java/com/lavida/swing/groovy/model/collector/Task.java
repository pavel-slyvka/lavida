package com.lavida.swing.groovy.model.collector;

import com.lavida.swing.groovy.model.Site;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Task implements Serializable {

	private static final long serialVersionUID = -7419468636945932247L;

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String script;
	private String scriptPath;
	
	@ManyToOne(cascade= CascadeType.ALL)
	private Site site;


	public Task() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScriptPath() {
		return scriptPath;
	}

	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
	
	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", name=" + name + ", script=" + script
				+ ", scriptPath=" + scriptPath + ", site=" + site + "]";
	}
	
}
