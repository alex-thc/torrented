package com.webapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.stil4m.transmission.api.domain.File;
import nl.stil4m.transmission.api.domain.TorrentInfo;

public class Item {
	private String uri;
	private Date addedDate;
	private long error;
	private String errorString;
	private long eta;
	private long id;
	private boolean isFinished;
	private boolean isStalled;
	private String name;
    private Long status;
    private Long totalSize;
    private List<String> fileNames;

    
	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public long getError() {
		return error;
	}

	public void setError(long error) {
		this.error = error;
	}

	public String getErrorString() {
		return errorString;
	}

	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}

	public long getEta() {
		return eta;
	}

	public void setEta(long eta) {
		this.eta = eta;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public boolean isStalled() {
		return isStalled;
	}

	public void setStalled(boolean isStalled) {
		this.isStalled = isStalled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
	}

	public List<String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}

	public Item() {
		// TODO Auto-generated constructor stub
	}
	
	public Item(String uri) {
		this.uri = uri;
	}
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public static Item fromTorrentInfo(TorrentInfo info) {
		Item item = new Item();
		
		item.setUri(info.getMagnetLink());
		item.setAddedDate(new Date(info.getAddedDate()*1000));
		item.setError(info.getError());
		item.setErrorString(info.getErrorString());
		item.setEta(info.getEta());
		item.setId(info.getId());
		item.setFinished(info.getFinished());
		item.setStalled(info.getStalled());
		item.setName(info.getName());
		item.setStatus(info.getStatus());
		item.setTotalSize(info.getTotalSize());
		List<String> files = null;
		if (info.getFiles() != null)
			files = new ArrayList<>();
		for(File file : info.getFiles())
			if (file.getName().matches("^.+?\\.mp4$")) {
				files.add(file.getName());
			}
		item.setFileNames(files);
		
		return item;
	}
	
}
