package com.webapp.controller.entity;

public class ItemStatus {
	private String hash;
    private Double percentDone;
    private String archiveFile;
    
	public ItemStatus(String hash, Double percentDone, String archiveFile) {
		super();
		this.hash = hash;
		this.percentDone = percentDone;
		this.archiveFile = archiveFile;
	}
}
