package com.webapp.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.webapp.controller.entity.ItemStatus;
import com.webapp.controller.entity.ResponseList;
import com.webapp.service.DownloadService;
import com.webapp.service.Item;
import com.webapp.service.entity.DownloadedItem;
import com.webapp.service.entity.UserEntry;
import com.webapp.service.entity.embedded.Session;
import com.webapp.service.repository.InvitationCodeRepository;
import com.webapp.service.repository.ItemRepository;
import com.webapp.service.repository.LinkRepository;
import com.webapp.service.repository.UserRepository;

import nl.stil4m.transmission.rpc.RpcException;

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
    
	@RequestMapping(value="/api/submitItem", method=RequestMethod.POST)
    public ResponseEntity<String> itemSubmit(
    		@RequestParam("newItemUri") String newItemUri,
    		@RequestParam("sessionId") String sessionId
    		) {		
		UserEntry user = userRepository.findBySessionId(sessionId);
		if (user == null) {
			System.out.println("Failed to get user by session: " + sessionId);
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		String hash = null;
		try {
			hash = downloadService.addItem(new Item(newItemUri), user);
		} catch (RpcException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		return ResponseEntity.ok(hash);
    }
	
	@RequestMapping(value="/api/getItemsStatus")
    public ResponseEntity<ResponseList> getItemsStatus(
    		@RequestParam("itemsHashes") List<String> itemsHashes,
    		@RequestParam("sessionId") String sessionId
    		) {	
		
		//System.out.println("Got request for: " + itemsHashes.toString());
		
		UserEntry user = userRepository.findBySessionId(sessionId);
		if (user == null) {
			System.out.println("Failed to get user by session: " + sessionId);
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		List<DownloadedItem> items = itemRepository.findUserItemsSorted(user, itemsHashes);
		
		ResponseList resList = new ResponseList();
		List<Object> statuses = 
				items
				.stream()
				.map(item -> 
				     new ItemStatus(item.getHash(), item.getName(), item.getPercentDone(), item.getArchiveFile()))
				.collect(Collectors.toList());
		resList.setList(statuses);
		
		return ResponseEntity.ok(resList);
    }
}
