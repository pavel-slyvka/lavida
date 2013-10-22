package com.lavida.swing.groovy.model.collector;

import java.io.Serializable;
import java.util.Date;

public class TaskExecutionHistory implements Serializable{

	private static final long serialVersionUID = -4373849369931640699L;
	
	private Long id;
	private Long taskId;
	private String stacktrace;
	private String pageLink;
	private Date taskExecutionTime;
	
	public TaskExecutionHistory() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getStacktrace() {
		return stacktrace;
	}

	public void setStacktrace(String stacktrace) {
		this.stacktrace = stacktrace;
	}

	public String getPageLink() {
		return pageLink;
	}

	public void setPageLink(String pageLink) {
		this.pageLink = pageLink;
	}

	
	public Date getTaskExecutionTime() {
		return taskExecutionTime;
	}

	public void setTaskExecutionTime(Date taskExecutionTime) {
		this.taskExecutionTime = taskExecutionTime;
	}

	@Override
	public String toString() {
		return "TaskExecutionHistory [id=" + id + ", taskId=" + taskId
				+ ", stacktrace=" + stacktrace + ", pageLink=" + pageLink
				+ ", taskExecutionTime=" + taskExecutionTime + "]";
	}

}
