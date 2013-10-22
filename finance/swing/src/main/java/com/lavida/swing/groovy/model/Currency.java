package com.lavida.swing.groovy.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Currency {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private Double buyValue;
	
	private Double sellValue;
	
	private Date date;
	
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

	public Double getBuyValue() {
		return buyValue;
	}

	public void setBuyValue(Double buyValue) {
		this.buyValue = buyValue;
	}

	public Double getSellValue() {
		return sellValue;
	}

	public void setSellValue(Double sellValue) {
		this.sellValue = sellValue;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
