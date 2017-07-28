package com.webapp.service.repository;

import java.util.Date;
import java.util.List;

import com.webapp.service.entity.DownloadedItem;
import com.webapp.service.entity.LinkEntry;
import com.webapp.service.entity.UserEntry;

public interface LinkRepositoryCustom {
	public void decrementLifeCounter(LinkEntry linkEntry);
	public long countNewUserLinksLast24h(String username);
}
