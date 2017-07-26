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
import com.webapp.service.entity.UserEntry;
import com.webapp.service.entity.embedded.Session;

public class UserRepositoryImpl implements UserRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void grantAccessToHash(UserEntry user, String hash) {
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(user.getUsername()));
		
		Update update = new Update();
		update.addToSet("torrentHashes", hash);
		
		mongoTemplate.updateFirst(query, update, UserEntry.class);
	}
	
	@Override
	public void addSessionObject(UserEntry user, Session session) {
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(user.getUsername()));
		
		Update update = new Update();
		update.push("sessions", session);
		
		mongoTemplate.updateFirst(query, update, UserEntry.class);
	}
	
}
