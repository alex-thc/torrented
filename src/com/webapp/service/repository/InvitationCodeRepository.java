package com.webapp.service.repository;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.webapp.service.entity.InvitationCode;
import com.webapp.service.entity.LinkEntry;

public interface InvitationCodeRepository extends MongoRepository<InvitationCode, String> {
	@Query("{ '_id':?0, 'validUntil': {$gte:?1}}") 
	InvitationCode findByIdAndDate(String id, Date date);
}

