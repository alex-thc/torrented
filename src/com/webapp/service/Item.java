package com.webapp.service;

public class Item {
	private String id;
	private String status;
	private String uri;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Item(String id, String status) {
		super();
		this.id = id;
		this.status = status;
	}
	public Item() {
		// TODO Auto-generated constructor stub
	}
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
	
}
