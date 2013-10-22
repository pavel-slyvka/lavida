package com.lavida.swing.groovy.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;


@Entity
public class Device implements Serializable{
	
	private static final long serialVersionUID = -5119700606153684269L;
	
	@Id
	@GeneratedValue
	private Long id;
	private String type;
	private String model;
	private String title;
	
	private String description;
	
	private String detailsLink;
	private String articul;
	
	private String promotion;
	private Double priceUAH;
	private Double priceUSD;
	private Boolean available;
	private boolean deleted;
	
	private String reviewsLink;
	private Double rating;
	
	@ManyToOne(cascade= CascadeType.ALL, fetch= FetchType.EAGER)
	private Site site;

	@ElementCollection
	@CollectionTable(name="DEVICE_CHARACTERISTICS")
	@MapKeyColumn(name="CHARACTERISTICS_NAME")
	@Column(name="CHARACTERISTICS_DESCRIPTION")
	private Map<String, String> characteristics = new LinkedHashMap<String, String>();
	
	public Device() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetailsLink() {
		return detailsLink;
	}

	public void setDetailsLink(String detailsLink) {
		this.detailsLink = detailsLink;
	}

	public String getArticul() {
		return articul;
	}

	public void setArticul(String articul) {
		this.articul = articul;
	}

	public String getPromotion() {
		return promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	public Double getPriceUAH() {
		return priceUAH;
	}

	public void setPriceUAH(Double priceUAH) {
		this.priceUAH = priceUAH;
	}

	public Double getPriceUSD() {
		return priceUSD;
	}

	public void setPriceUSD(Double priceUSD) {
		this.priceUSD = priceUSD;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getReviewsLink() {
		return reviewsLink;
	}

	public void setReviewsLink(String reviewsLink) {
		this.reviewsLink = reviewsLink;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, String> getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(Map<String, String> characteristics) {
		this.characteristics = characteristics;
	}

	@Override
	public String toString() {
		return "Device [id=" + id + ", type=" + type + ", model=" + model
				+ ", articul=" + articul + ", priceUAH=" + priceUAH
				+ ", priceUSD=" + priceUSD + ", available=" + available
				+ ", promotion=" + promotion
				+ ", title=" + title + ", description=" + description
				+ ", detailsLink=" + detailsLink 
				+ ", deleted=" + deleted + ", reviewsLink=" + reviewsLink
				+ ", rating=" + rating + ", site=" + site
				+ ", characteristics=" + characteristics + "]";
	}

}
