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
}
