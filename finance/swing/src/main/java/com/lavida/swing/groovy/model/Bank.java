package com.lavida.swing.groovy.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Bank {

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	private String address;
	private String description;
	
	@OneToMany(cascade = CascadeType.ALL, fetch= FetchType.EAGER)
	private List<Currency> currencyList = new ArrayList<Currency>();
	
	@ElementCollection(fetch= FetchType.EAGER)
	@CollectionTable(name="BANK_WORKINGHOURS")
	@MapKeyColumn(name="DAY_OF_WEEK")
	@Column(name="HOURS")
	private Map<String, String> bankWorkingHours = new LinkedHashMap<String, String>();

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Currency> getCurrencyList() {
		return currencyList;
	}

	public void setCurrencyList(List<Currency> currencyList) {
		this.currencyList = currencyList;
	}

	public Map<String, String> getBankWorkingHours() {
		return bankWorkingHours;
	}

	public void setBankWorkingHours(Map<String, String> bankWorkingHours) {
		this.bankWorkingHours = bankWorkingHours;
	}

	@Override
	public String toString() {
		return "Bank [id=" + id + ", name=" + name + ", address=" + address
				+ ", description=" + description + ", currencyList="
				+ currencyList + ", bankWorkingHours=" + bankWorkingHours + "]";
	}
	
}
