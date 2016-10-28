package com.webapp.service.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.webapp.service.entity.LinkEntry;

public interface LinkRepository extends MongoRepository<LinkEntry, UUID> {

}

