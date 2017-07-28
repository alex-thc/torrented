package com.webapp.service.repository;

import java.util.Date;
import java.util.List;

import com.webapp.service.entity.DownloadedItem;
import com.webapp.service.entity.UserEntry;

public interface ActivityRepositoryCustom {
	public long countNewUserItemsLast24h(String username);
}
