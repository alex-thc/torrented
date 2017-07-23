package com.webapp.service.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.webapp.util.Constants;

@Document
public class UserEntry {
	@Id
	private String username;
	private String password;
	
	List<Constants.UserGroup> groups; //permission groups
	List<String> torrentHashes; //torrent hashes that this user has access to (uppercase)
	
	public List<String> getTorrentHashes() {
		return torrentHashes;
	}
	public void setTorrentHashes(List<String> torrentHashes) {
		this.torrentHashes = torrentHashes;
	}
	public List<Constants.UserGroup> getGroups() {
		return groups;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public UserEntry() {
		
	}
}
