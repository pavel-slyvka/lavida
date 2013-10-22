package com.lavida.swing.groovy.model.collector;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Event {

	@Id
	@GeneratedValue
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date eventDate;
	private String eventType;

	@Enumerated(EnumType.STRING)
	private EventLevel level;

	private String eventContent;

	public Event() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public EventLevel getLevel() {
		return level;
	}

	public void setLevel(EventLevel level) {
		this.level = level;
	}

	public String getEventContent() {
		return eventContent;
	}

	public void setEventContent(String eventContent) {
		this.eventContent = eventContent;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", eventDate=" + eventDate + ", eventType="
				+ eventType + ", level=" + level + ", eventContent="
				+ eventContent + "]";
	}
	

}
