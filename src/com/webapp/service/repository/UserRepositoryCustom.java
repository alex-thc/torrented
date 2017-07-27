package com.webapp.service.repository;

import java.util.Date;
import java.util.List;

import com.webapp.service.entity.DownloadedItem;
import com.webapp.service.entity.UserEntry;
import com.webapp.service.entity.embedded.Session;

public interface UserRepositoryCustom {
	public void grantAccessToHash(UserEntry user, String hash);
	public void addSessionObject(UserEntry user, Session session);
	public void removeExpiredSessions(Date date);
	public UserEntry findBySessionId(String sessionId);
}
