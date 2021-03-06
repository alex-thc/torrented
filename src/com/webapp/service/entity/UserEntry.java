package com.webapp.service.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.webapp.service.entity.embedded.Session;
import com.webapp.util.Constants;

@Document
public class UserEntry {
	@Id
	private String username;
	private String password;
	
	List<Constants.UserGroup> groups; //permission groups
	List<String> torrentHashes; //torrent hashes that this user has access to (uppercase)
	
    private List<Session> sessions;
    
    private int itemsDayLimit = Constants.NEW_ITEMS_PER_DAY_LIMIT;
	
	public List<String> getTorrentHashes() {
		return torrentHashes;
	}
	public void setTorrentHashes(List<String> torrentHashes) {
		this.torrentHashes = torrentHashes;
	}
	public List<Constants.UserGroup> getGroups() {
		return groups;
	}
	public void setGroups(List<Constants.UserGroup> groups) {
		this.groups = groups;
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
	
	public List<Session> getSessions() {
		return sessions;
	}
	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}
	public int getItemsDayLimit() {
		return itemsDayLimit;
	}
	public void setItemsDayLimit(int itemsDayLimit) {
		this.itemsDayLimit = itemsDayLimit;
	}
}
