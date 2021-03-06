package com.webapp.service.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import nl.stil4m.transmission.api.domain.File;
import nl.stil4m.transmission.api.domain.TorrentInfo;

@Document
public class DownloadedItem {
	//fields from TorrentInfo
	private String magnetLink;
	private Date addedDate;
	private long error;
	private String errorString;
	@Id
	private String id;
	private boolean isFinished;
	private String name;
    private Long status;
    private Long totalSize;
        
    //active stats
    private Long eta;
    private Double percentDone;
    private Long rateDownload;
    
    //hash of the torrent
    private String hash;
    
    //all files in the torrent
    private List<String> downloadedFiles;
    
    //files that need conversion
    private List<String> filesToConvert;

	//files created by the conversion process (not used)
    private List<String> convertedFiles;
    
    //video files
    private List<String> videoFiles;
    
    //being processed by the converter or the archiver
    private boolean isProcessing;
    private String processingStatus;
    
    //is an active download
    private boolean isActive;
    
    //archive error (null when it was a success)
    private String archiveError;
    
    //archive file
    private String archiveFile;
    
	public Long getEta() {
		return eta;
	}

	public void setEta(Long eta) {
		this.eta = eta;
	}

	public Double getPercentDone() {
		return percentDone;
	}

	public void setPercentDone(Double percentDone) {
		this.percentDone = percentDone;
	}

	public Long getRateDownload() {
		return rateDownload;
	}

	public void setRateDownload(Long rateDownload) {
		this.rateDownload = rateDownload;
	}

	public boolean isProcessing() {
		return isProcessing;
	}

	public void setProcessing(boolean isProcessing) {
		this.isProcessing = isProcessing;
	}

	public String getMagnetLink() {
		return magnetLink;
	}

	public void setMagnetLink(String magnetLink) {
		this.magnetLink = magnetLink;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

    public List<String> getFilesToConvert() {
		return filesToConvert;
	}

	public void setFilesToConvert(List<String> filesToConvert) {
		this.filesToConvert = filesToConvert;
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

	public DownloadedItem() {
		// TODO Auto-generated constructor stub
	}
	
	public static DownloadedItem fromTorrentInfo(TorrentInfo info, boolean isActive) {
		DownloadedItem item = new DownloadedItem();
		
		item.setActive(isActive);
		
		item.setMagnetLink(info.getMagnetLink());
		item.setAddedDate(new Date(info.getAddedDate()*1000));
		item.setError(info.getError());
		item.setErrorString(info.getErrorString());
		item.setId(info.getHashString().toUpperCase()); //use hash as a unique id (upper case)
		item.setFinished(info.getFinished());
		item.setName(info.getName());
		item.setStatus(info.getStatus());
		item.setTotalSize(info.getTotalSize());
		
		item.setHash(info.getHashString().toUpperCase());
		
		item.setEta(info.getEta());
		item.setRateDownload(info.getRateDownload());
		item.setPercentDone(info.getPercentDone());
		
		if (! isActive && info.getFiles() != null) { //no point in parsing files if it's an active download
			List<String> downloadedFiles = new ArrayList<>();
			List<String> videoFiles = new ArrayList<>();
			List<String> filesToConvert = new ArrayList<>();
			
		    for(File file : info.getFiles()) {
		    	downloadedFiles.add(file.getName());
		    	
		    	if (file.getName().matches("^.+?\\.mp4$")) {
		    		videoFiles.add(file.getName());
				}
		    	
		    	if (file.getName().matches("^.+?\\.(avi|mkv)$")) {
		    		filesToConvert.add(file.getName());
				}
		    }
		    	
		    item.setDownloadedFiles(downloadedFiles);
		    if (! videoFiles.isEmpty())
		    	item.setVideoFiles(videoFiles);
		    if (! filesToConvert.isEmpty())
		    	item.setFilesToConvert(filesToConvert);
		}
		
		return item;
	}

	public String getProcessingStatus() {
		return processingStatus;
	}

	public void setProcessingStatus(String processingStatus) {
		this.processingStatus = processingStatus;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getArchiveError() {
		return archiveError;
	}

	public void setArchiveError(String archiveError) {
		this.archiveError = archiveError;
	}

	public String getArchiveFile() {
		return archiveFile;
	}

	public void setArchiveFile(String archiveFile) {
		this.archiveFile = archiveFile;
	}
	
}
