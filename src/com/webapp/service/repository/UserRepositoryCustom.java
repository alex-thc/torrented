package com.webapp.service.repository;

import java.util.Date;
import java.util.List;

import com.webapp.service.entity.DownloadedItem;
import com.webapp.service.entity.UserEntry;

public interface UserRepositoryCustom {
	public void grantAccessToHash(UserEntry user, String hash);
}
