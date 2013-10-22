package com.lavida.swing.groovy.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class DeviceHistory implements Serializable{
	
	private static final long serialVersionUID = -1825181811023952017L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	private Long deviceId;
	
	private String articul;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date historyDate;
	
	private String promotion;
	private Double priceUAH;
	private Double priceUSD;
	private Boolean available;
	private boolean deleted;
	
	private Double rating;


	public DeviceHistory() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
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

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Date getHistoryDate() {
		return historyDate;
	}

	public void setHistoryDate(Date historyDate) {
		this.historyDate = historyDate;
	}

	@Override
	public String toString() {
		return "DeviceHistory [id=" + id + ", deviceId=" + deviceId
				+ ", articul=" + articul + ", historyDate=" + historyDate
				+ ", promotion=" + promotion + ", priceUAH=" + priceUAH
				+ ", priceUSD=" + priceUSD + ", available=" + available
				+ ", deleted=" + deleted + ", rating=" + rating + "]";
	}
	
}
