package com.lavida.swing.groovy.model;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Site {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	private String phone;
	private String contacts;
	
	@ElementCollection(fetch= FetchType.EAGER)
	@CollectionTable(name="SITE_WORKINGHOURS")
	@MapKeyColumn(name="DAY_OF_WEEK")
	@Column(name="HOURS")
	private Map<String, String> workingHours = new LinkedHashMap<String, String>();
	
	public Site() {
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public Map<String, String> getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(Map<String, String> workingHours) {
		this.workingHours = workingHours;
	}

	@Override
	public String toString() {
		return "Site [id=" + id + ", name=" + name + ", phone=" + phone
				+ ", contacts=" + contacts + ", workingHours=" + workingHours
				+ "]";
	}
}
