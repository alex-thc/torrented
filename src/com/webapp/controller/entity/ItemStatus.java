package com.webapp.controller.entity;

public class ItemStatus {
	private String hash;
	private String name;
    private Double percentDone;
    private String archiveFile;
    
	public ItemStatus(String hash, String name, Double percentDone, String archiveFile) {
		super();
		this.hash = hash;
		this.name = name;
		this.percentDone = percentDone;
		this.archiveFile = archiveFile;
	}
}
