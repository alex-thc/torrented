package com.webapp.service.entity.embedded;

import java.util.Date;
import java.util.UUID;

public class Session {
	private String id;
	private Date dateCreated;
	
	public Session() {
		id = UUID.randomUUID().toString();
		dateCreated = new Date();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
}
