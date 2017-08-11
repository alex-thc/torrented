package com.webapp.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.webapp.service.DownloadService;
import com.webapp.service.entity.UserEntry;
import com.webapp.service.entity.embedded.Session;
import com.webapp.service.repository.InvitationCodeRepository;
import com.webapp.service.repository.ItemRepository;
import com.webapp.service.repository.LinkRepository;
import com.webapp.service.repository.UserRepository;

@RestController
public class ApiController {

	@Autowired
	private LinkRepository linkRepository;
	
	@Autowired
	private DownloadService downloadService;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private InvitationCodeRepository invitationCodeRepository;
	
    @RequestMapping("/api/getSession")
    public ResponseEntity<Session> getSession(
    		@RequestParam(value="user") String user,
    		@RequestParam(value="password") String password) {
		UserEntry userInfo = userRepository.findOne(user);
		
		if (userInfo == null || ! userInfo.getPassword().equals(password)) {
			System.out.println("Failure to authenticate as " + user);
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		Session session = new Session();
		userRepository.addSessionObject(userInfo, session);
		
		return ResponseEntity.ok(session);
    }
}
