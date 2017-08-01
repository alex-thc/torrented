package com.webapp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.expression.spel.ast.Ternary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.webapp.service.DownloadService;
import com.webapp.service.Item;
import com.webapp.service.LoginInfo;
import com.webapp.service.entity.ActivityEntry;
import com.webapp.service.entity.DownloadedItem;
import com.webapp.service.entity.LinkEntry;
import com.webapp.service.entity.UserEntry;
import com.webapp.service.entity.embedded.Session;
import com.webapp.service.repository.ActivityRepository;
import com.webapp.service.repository.InvitationCodeRepository;
import com.webapp.service.repository.ItemRepository;
import com.webapp.service.repository.LinkRepository;
import com.webapp.service.repository.UserRepository;
import com.webapp.util.Constants;

import nl.stil4m.transmission.api.domain.TorrentInfo;
import nl.stil4m.transmission.rpc.RpcException;

@Controller
public class WebAppHelloWorld {
	
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
	
	@RequestMapping("/welcome")
	public ModelAndView helloWorld(@RequestParam("file") String file, 
			@CookieValue(value = "session", defaultValue = "") String sessionId) {
		
		UserEntry user = userRepository.findBySessionId(sessionId);
		if (user == null) {
			System.out.println("Failed to get user by session: " + sessionId);
			return new ModelAndView(new RedirectView("login"));
		}
		
		if (linkRepository.countNewUserLinksLast24h(user.getUsername()) >= Constants.NEW_LINKS_PER_DAY_LIMIT) {
			return new ModelAndView("error","message","Exceeded number of new links per day");
		}
	
		LinkEntry linkEntry = new LinkEntry(file, Constants.FileType.FILE_VIDEO, Constants.VIDEO_LINK_LIVES, user.getUsername());
		linkRepository.save(linkEntry);
		
		Map<String, Object> model = new HashMap<>();
		model.put("id", linkEntry.getId().toString().replaceAll("-", ""));
		model.put("file", file);

		return new ModelAndView("welcome", "model", model);
	}
	
	@RequestMapping("/download")
	public ModelAndView download(
			@RequestParam("file") String file, 
			@CookieValue(value = "session", defaultValue = "") String sessionId) {
	
		//first, make sure we're logged in
		UserEntry user = userRepository.findBySessionId(sessionId);
		if (user == null) {
			System.out.println("Failed to get user by session: " + sessionId);
			return new ModelAndView(new RedirectView("login"));
		}
		
		if (linkRepository.countNewUserLinksLast24h(user.getUsername()) >= Constants.NEW_LINKS_PER_DAY_LIMIT) {
			return new ModelAndView("error","message","Exceeded number of new links per day");
		}
		
		//generate the link entry
		LinkEntry linkEntry = new LinkEntry(file, Constants.FileType.FILE_ARCHIVE, Constants.ARCHIVE_LINK_LIVES, user.getUsername());
		linkRepository.save(linkEntry);
		
		Map<String, Object> model = new HashMap<>();
		model.put("id", linkEntry.getId().toString().replaceAll("-", ""));
		model.put("file", file);

		return new ModelAndView("download", "model", model);
	}
	
	@RequestMapping("/")
	public ModelAndView mainView(
			@CookieValue(value = "session", defaultValue = "") String sessionId
			) {
 
		UserEntry user = userRepository.findBySessionId(sessionId);
		if (user == null) {
			System.out.println("Failed to get user by session: " + sessionId);
			return new ModelAndView(new RedirectView("login"));
		}
		
		ModelAndView model = new ModelAndView("index");
		
		if (user.getGroups().contains(Constants.UserGroup.GROUP_ADMIN))
		{
			model.addObject("showActiveDownloads", true);
			
			List<TorrentInfo> activeDownloads = null;
			
			try {
				activeDownloads = downloadService.getAllItems();
			} catch (RpcException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			model.addObject("activeDownloadsList", activeDownloads);
		}
		
		List<DownloadedItem> activeItems = itemRepository.findUserItemsSorted(user, true /*active*/);;
		
		model.addObject("activeItemsList", activeItems);
		
		List<DownloadedItem> downloadedItems = null;
		
		if (user.getGroups().contains(Constants.UserGroup.GROUP_ADMIN)) //admin sees it all
			downloadedItems = itemRepository.findAllNonActive();
		else if (user.getGroups().contains(Constants.UserGroup.GROUP_USER)) {
			downloadedItems = itemRepository.findUserItemsSorted(user, false /*not active*/);
		}
		
		model.addObject("downloadedItemsList", downloadedItems);
		
		return model;
	}
	
	@RequestMapping(value="/submit", method=RequestMethod.POST)
    public ModelAndView itemSubmit(
    		@RequestParam("newItemUri") String newItemUri,
    		@CookieValue(value = "session", defaultValue = "") String sessionId
    		) {
		System.out.println("Item uri: " + newItemUri + " " + downloadService.getMessage());
		
//		UserEntry user = userRepository.findOne(username);
//		if (user == null) {
//			new ModelAndView("submit", "error", "Failed to get user by username: " + username);
//		}
		UserEntry user = userRepository.findBySessionId(sessionId);
		if (user == null) {
			System.out.println("Failed to get user by session: " + sessionId);
			return new ModelAndView(new RedirectView("login"));
		}
		
		try {
			downloadService.addItem(new Item(newItemUri), user);
		} catch (RpcException e) {
			return new ModelAndView("submit", "error", e.getMessage());
		}
		
		return new ModelAndView("submit", "error", null);
    }

	@RequestMapping(value="/login",method=RequestMethod.GET)
	public ModelAndView displayLogin(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView model = new ModelAndView("login");
		LoginInfo loginInfo = new LoginInfo();
		model.addObject("loginInfo", loginInfo);
		return model;
	}
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public ModelAndView executeLogin(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("loginInfo")LoginInfo loginInfo)
	{
		UserEntry userInfo = userRepository.findOne(loginInfo.getUsername());
		
		if (userInfo == null || ! userInfo.getPassword().equals(loginInfo.getPassword())) {
			System.out.println("Failure to authenticate as " + loginInfo.getUsername());
			ModelAndView model = new ModelAndView("login");
			model.addObject("loginInfo", loginInfo);
			request.setAttribute("message", "Invalid credentials!!");
			return model;
		}
		
		Session session = new Session();
		userRepository.addSessionObject(userInfo, session);
		
		Cookie authCookie = new Cookie("session", session.getId());
		authCookie.setMaxAge(3600*24);
		response.addCookie(authCookie);
		
		//return mainView(session.getId());
		return new ModelAndView(new RedirectView("/WebAppTest"));
	}
	
	@RequestMapping(value="/register",method=RequestMethod.GET)
	public ModelAndView displayRegisterForm(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView model = new ModelAndView("register");
		LoginInfo loginInfo = new LoginInfo();
		model.addObject("loginInfo", loginInfo);
		return model;
	}
	
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public ModelAndView executeRegister(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("loginInfo")LoginInfo loginInfo)
	{
		//check the inputs
		if (
		  loginInfo.getUsername() == null || loginInfo.getUsername().trim().isEmpty() ||
		  loginInfo.getPassword() == null || loginInfo.getPassword().trim().isEmpty()
		)
		{
			ModelAndView model = new ModelAndView("register");
			model.addObject("loginInfo", loginInfo);
			request.setAttribute("message", "Username and password cannot be empty");
			return model;
		}
		
		if (loginInfo.getInviteCode() == null || loginInfo.getInviteCode().trim().isEmpty())
		{
			ModelAndView model = new ModelAndView("register");
			model.addObject("loginInfo", loginInfo);
			request.setAttribute("message", "Sorry, but this stuff is invite-only for now");
			return model;
		}
		
		if (invitationCodeRepository.findByIdAndDate(loginInfo.getInviteCode().trim(), new Date()) == null) {
			ModelAndView model = new ModelAndView("register");
			model.addObject("loginInfo", loginInfo);
			request.setAttribute("message", "Your invite code is not valid");
			return model;
		}
		
		if (userRepository.findByUsername(loginInfo.getUsername()) != null) {
			ModelAndView model = new ModelAndView("register");
			model.addObject("loginInfo", loginInfo);
			request.setAttribute("message", "The user with this name already exists");
			return model;
		}
		
		//construct a new user object
		UserEntry userInfo = new UserEntry();
		userInfo.setUsername(loginInfo.getUsername());
		userInfo.setPassword(loginInfo.getPassword());
		userInfo.setGroups(Arrays.asList(Constants.UserGroup.GROUP_USER));
		
		//add a session right away
		Session session = new Session();
		userInfo.setSessions(Arrays.asList(session));
		
		userRepository.insert(userInfo);
			
		Cookie authCookie = new Cookie("session", session.getId());
		authCookie.setMaxAge(3600*24);
		response.addCookie(authCookie);

		return new ModelAndView(new RedirectView("/WebAppTest"));
	}
}
