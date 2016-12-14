package com.webapp.service.repository;

import java.io.NotActiveException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	
}
