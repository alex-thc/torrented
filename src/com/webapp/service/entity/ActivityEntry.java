package com.webapp.service.entity;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import com.webapp.util.Constants;
import com.webapp.util.Constants.ActivityType;

@Document
public class ActivityEntry {
	private Constants.ActivityType type;
	private Date ts;
	private String user;
	
	public Constants.ActivityType getType() {
		return type;
	}
	public void setType(Constants.ActivityType type) {
		this.type = type;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	
	public ActivityEntry() {
		
	}
	
	public ActivityEntry(ActivityType type, String user) {
		super();
		this.type = type;
		this.ts = new Date();
		this.setUser(user);
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}

}
