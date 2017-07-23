package com.webapp.service.repository;

import java.io.NotActiveException;
import java.util.Date;
import java.util.List;

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

public class ItemRepositoryImpl implements ItemRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public DownloadedItem getAndLockItemToConvert() {
		Query query = new Query();
		query.addCriteria(Criteria.where("filesToConvert").ne(null).and("isProcessing").is(false));

		Update update = new Update();
		update.set("isProcessing", true);

		return mongoTemplate.findAndModify(
				query, update,
				new FindAndModifyOptions().returnNew(true), DownloadedItem.class);
	}

	@Override
	public List<DownloadedItem> getItemsAddedBeforeDate(Date date) {
		Query query = new Query();
		query.addCriteria(Criteria.where("addedDate").lte(date).and("isProcessing").is(false));

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
	public List<DownloadedItem> findUserItemsSorted(String user) {
		Query query = new Query();
		query.addCriteria(Criteria.where("users").is(user));

		return mongoTemplate.find(query.with(new Sort(Direction.DESC, "addedDate")), DownloadedItem.class);
	}
}
