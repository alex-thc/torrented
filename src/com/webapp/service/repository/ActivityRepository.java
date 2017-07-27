package com.webapp.service.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.webapp.service.entity.ActivityEntry;
import com.webapp.service.entity.LinkEntry;

public interface ActivityRepository extends MongoRepository<ActivityEntry, String> {

}

