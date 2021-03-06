package com.webapp.service.repository;

import java.io.NotActiveException;
import java.util.Calendar;
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
import com.webapp.service.entity.LinkEntry;
import com.webapp.service.entity.UserEntry;

public class LinkRepositoryImpl implements LinkRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void decrementLifeCounter(LinkEntry linkEntry) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(linkEntry.getId()));
		
		Update update = new Update();
		update.inc("lifeCounter", -1);
		
		mongoTemplate.updateFirst(query, update, LinkEntry.class);
	}

	@Override
	public long countNewUserLinksLast24h(String username) {
		Calendar calendar = Calendar.getInstance(); // this would default to now
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		
		Query query = new Query();
		query.addCriteria(Criteria.where("user").is(username).and("createdDate").gte(calendar.getTime()));
		
		return mongoTemplate.count(query, LinkEntry.class);
	}
}
