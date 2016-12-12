package com.webapp.service.entity;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class LinkEntry {
	@Id
	private UUID id;
	
	private String link;
	
	@Indexed(expireAfterSeconds=86400)
	private Date createdDate;

	public UUID getId() {
		return id;
	}

	public String getLink() {
		return link;
	}

	public LinkEntry(String link) {
		this.link = link;
		this.id = UUID.randomUUID();
		this.createdDate = new Date();
	}
	
}
