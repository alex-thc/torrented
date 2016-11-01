package com.webapp.service.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import nl.stil4m.transmission.api.domain.File;
import nl.stil4m.transmission.api.domain.TorrentInfo;

@Document
public class Item {
	//fields from TorrentInfo
	private String uri;
	private Date addedDate;
	private long error;
	private String errorString;
	@Id
	private long id;
	private boolean isFinished;
	private String name;
    private Long status;
    private Long totalSize;
    
    //flagged for conversion
    boolean needsConv;
    
    //all files in the torrent
    private List<String> downloadedFiles;
    //files created by the conversion process
    private List<String> convertedFiles;
    
    //video files
    private List<String> videoFiles;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

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

	public boolean isNeedsConv() {
		return needsConv;
	}

	public void setNeedsConv(boolean needsConv) {
		this.needsConv = needsConv;
	}

	public List<String> getDownloadedFiles() {
		return downloadedFiles;
	}

	public void setDownloadedFiles(List<String> downloadedFiles) {
		this.downloadedFiles = downloadedFiles;
	}

	public List<String> getConvertedFiles() {
		return convertedFiles;
	}

	public void setConvertedFiles(List<String> convertedFiles) {
		this.convertedFiles = convertedFiles;
	}

	public List<String> getVideoFiles() {
		return videoFiles;
	}

	public void setVideoFiles(List<String> videoFiles) {
		this.videoFiles = videoFiles;
	}

	public Item() {
		// TODO Auto-generated constructor stub
	}
	
	public static Item fromTorrentInfo(TorrentInfo info) {
		Item item = new Item();
		
		item.setUri(info.getMagnetLink());
		item.setAddedDate(new Date(info.getAddedDate()*1000));
		item.setError(info.getError());
		item.setErrorString(info.getErrorString());
		item.setId(info.getId());
		item.setFinished(info.getFinished());
		item.setName(info.getName());
		item.setStatus(info.getStatus());
		item.setTotalSize(info.getTotalSize());
		
		if (info.getFiles() != null) {
			List<String> files = new ArrayList<>();
		    for(File file : info.getFiles())
			    files.add(file.getName());
		    item.setDownloadedFiles(files);
		}
		
		return item;
	}
	
}
