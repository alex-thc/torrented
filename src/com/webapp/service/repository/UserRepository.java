package com.webapp.service.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.webapp.service.entity.LinkEntry;
import com.webapp.service.entity.UserEntry;

public interface UserRepository extends MongoRepository<UserEntry, String>, UserRepositoryCustom {

}

