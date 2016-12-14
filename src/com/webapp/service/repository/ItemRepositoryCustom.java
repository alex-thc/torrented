package com.webapp.service.repository;

import java.util.Date;
import java.util.List;

import com.webapp.service.entity.DownloadedItem;

public interface ItemRepositoryCustom {
	public DownloadedItem getAndLockItemToConvert();
	public List<DownloadedItem> getItemsAddedBeforeDate(Date date);
}
