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

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPercentDone() {
		return percentDone;
	}

	public void setPercentDone(Double percentDone) {
		this.percentDone = percentDone;
	}

	public String getArchiveFile() {
		return archiveFile;
	}

	public void setArchiveFile(String archiveFile) {
		this.archiveFile = archiveFile;
	}
	
	
}
