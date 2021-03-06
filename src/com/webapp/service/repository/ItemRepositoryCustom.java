package com.webapp.service.repository;

import java.util.Date;
import java.util.List;

import com.webapp.service.entity.DownloadedItem;
import com.webapp.service.entity.UserEntry;

public interface ItemRepositoryCustom {
	public DownloadedItem getAndLockNonActiveItemToConvert();
	public List<DownloadedItem> getNonActiveItemsAddedBeforeDate(Date date);
	public List<DownloadedItem> getItemsInProcess();
	public void setItemProcessingStatus(DownloadedItem item, String processingStatus);
	
	public List<DownloadedItem> findUserItemsSorted(UserEntry user, boolean isActive);
	
	public List<DownloadedItem> findUserItemsSorted(UserEntry user, List<String> hashes);
	
	public void resetAddedDate(DownloadedItem item, Date date);
	
	public DownloadedItem getItemActiveFlagById(String id);
	
	public List<DownloadedItem> findAllNonActive();
	
	public DownloadedItem getAndLockNonActiveItemToArchive();
}
