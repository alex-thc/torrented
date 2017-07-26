package com.webapp.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapp.exception.ShellCommandException;
import com.webapp.service.entity.DownloadedItem;
import com.webapp.service.repository.ItemRepository;
import com.webapp.service.repository.LinkRepository;
import com.webapp.service.repository.UserRepository;
import com.webapp.util.Constants;
import com.webapp.util.ShellCommand;
import com.webapp.util.Util;

@Service
public class SessionCleanupService {
	@Autowired
	private UserRepository userRepository;
		
	private static final Integer EXPIRY_DAYS = 2;
	
	public void runCleanupRound() {
		Calendar calendar = Calendar.getInstance(); // this would default to now
		calendar.add(Calendar.DAY_OF_MONTH, -EXPIRY_DAYS);
		userRepository.removeExpiredSessions(calendar.getTime());
	}
}
