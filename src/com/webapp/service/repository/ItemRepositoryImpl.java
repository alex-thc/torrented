package com.webapp.service.repository;

import java.io.NotActiveException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.webapp.service.entity.DownloadedItem;
import com.webapp.service.entity.UserEntry;

public class ItemRepositoryImpl implements ItemRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public DownloadedItem getAndLockNonActiveItemToConvert() {
		Query query = new Query();
		query.addCriteria(Criteria.where("filesToConvert").ne(null).and("isProcessing").is(false).and("isActive").is(false));

		Update update = new Update();
		update.set("isProcessing", true);

		return mongoTemplate.findAndModify(
				query, update,
				new FindAndModifyOptions().returnNew(true), DownloadedItem.class);
	}

	@Override
	public DownloadedItem getAndLockNonActiveItemToArchive() {
		Query query = new Query();
		query.addCriteria(Criteria.where("archiveFile").is(null).and("archiveError").is(null).
				and("isProcessing").is(false).and("isActive").is(false));

		Update update = new Update();
		update.set("isProcessing", true);
		update.set("processingStatus", "Archiving");

		return mongoTemplate.findAndModify(
				query, update,
				new FindAndModifyOptions().returnNew(true), DownloadedItem.class);
	}

	
	@Override
	public List<DownloadedItem> getNonActiveItemsAddedBeforeDate(Date date) {
		Query query = new Query();
		query.addCriteria(Criteria.where("addedDate").lte(date).and("isProcessing").is(false).and("isActive").is(false));

		return mongoTemplate.find(query, DownloadedItem.class);
	}

	@Override
	public List<DownloadedItem> getItemsInProcess() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isProcessing").is(true));

		return mongoTemplate.find(query, DownloadedItem.class);
	}

	@Override
	public void setItemProcessingStatus(DownloadedItem item, String processingStatus) {
		Query query = new Query();
		query.addCriteria(Criteria.where("isProcessing").is(true).and("_id").is(item.getId())); //we need to make sure that we don't conflict with the conversion thread resetting the status
		
		Update update = new Update();
		update.set("processingStatus", processingStatus);
		
		mongoTemplate.updateFirst(query, update, DownloadedItem.class);
	}
	
	@Override
	public void resetAddedDate(DownloadedItem item, Date date) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(item.getId()));
		
		Update update = new Update();
		update.set("addedDate", date);
		
		mongoTemplate.updateFirst(query, update, DownloadedItem.class);
	}
	
	@Override
	public List<DownloadedItem> findUserItemsSorted(UserEntry user, boolean isActive) {
		Query query = new Query();
		
		if (user.getTorrentHashes() == null)
			return null;
		
		query.addCriteria(Criteria.where("hash").in(user.getTorrentHashes()).and("isActive").is(isActive));

		return mongoTemplate.find(query.with(new Sort(Direction.DESC, "addedDate")), DownloadedItem.class);
	}
	
	@Override
	public List<DownloadedItem> findUserItemsSorted(UserEntry user, List<String> hashes) {
		Query query = new Query();
		
		if (user.getTorrentHashes() == null)
			return null;
		
		List<String> hashesToSearch = 
		   hashes.stream().filter(hash -> user.getTorrentHashes().contains(hash)).collect(Collectors.toList());
		
		query.addCriteria(Criteria.where("hash").in(hashesToSearch));

		return mongoTemplate.find(query.with(new Sort(Direction.DESC, "addedDate")), DownloadedItem.class);
	}

	@Override
	public DownloadedItem getItemActiveFlagById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		
		//we are only interested in the isActive flag
		query.fields().include("isActive");

		return mongoTemplate.findOne(query, DownloadedItem.class);
	}
	
	@Override
	public List<DownloadedItem> findAllNonActive() {
		Query query = new Query();
		
		query.addCriteria(Criteria.where("isActive").is(false));

		return mongoTemplate.find(query.with(new Sort(Direction.DESC, "addedDate")), DownloadedItem.class);
	}
}
