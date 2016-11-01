package com.webapp.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.webapp.service.entity.Item;

public interface ItemRepository extends MongoRepository<Item, Long> {

}
