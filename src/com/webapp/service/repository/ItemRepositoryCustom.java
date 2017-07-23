package com.webapp.service.repository;

import java.util.Date;
import java.util.List;

import com.webapp.service.entity.DownloadedItem;
import com.webapp.service.entity.UserEntry;

public interface ItemRepositoryCustom {
	public DownloadedItem getAndLockItemToConvert();
	public List<DownloadedItem> getItemsAddedBeforeDate(Date date);
	public List<DownloadedItem> getItemsInProcess();
	public void setItemProcessingStatus(DownloadedItem item, String processingStatus);
	
	public List<DownloadedItem> findUserItemsSorted(UserEntry user);
	
	public void resetAddedDate(DownloadedItem item, Date date);
}
