package com.webapp.service.repository;

import com.webapp.service.entity.DownloadedItem;

public interface ItemRepositoryCustom {
	public DownloadedItem getAndLockItemToConvert();
}
