package com.webapp.service.entity;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.webapp.util.Constants;

@Document
public class LinkEntry {
	@Id
	private UUID id;
	
	private String link;
	
	private Constants.FileType type; //archive or video
	private int lifeCounter; //how many lives we have (-1 - unlimited)
	
	@Indexed(expireAfterSeconds=86400)
	private Date createdDate;
	

	public UUID getId() {
		return id;
	}

	public String getLink() {
		return link;
	}

	public LinkEntry(String link, Constants.FileType type, int lifeCounter) {
		this.link = link;
		this.id = UUID.randomUUID();
		this.createdDate = new Date();
		this.type = type;
		this.lifeCounter = lifeCounter;
	}

	public Constants.FileType getType() {
		return type;
	}

	public void setType(Constants.FileType type) {
		this.type = type;
	}
	
}
