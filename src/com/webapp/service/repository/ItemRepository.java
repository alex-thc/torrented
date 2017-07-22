package com.webapp.service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.webapp.service.entity.DownloadedItem;

public interface ItemRepository extends MongoRepository<DownloadedItem, Long>,ItemRepositoryCustom {
	
}
