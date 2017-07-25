package com.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.webapp.service.entity.DownloadedItem;
import com.webapp.service.entity.LinkEntry;
import com.webapp.service.entity.UserEntry;
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
	
	@RequestMapping("/welcome")
	public ModelAndView helloWorld(@RequestParam("file") String file) {
	
		LinkEntry linkEntry = new LinkEntry(file, Constants.FileType.FILE_VIDEO, -1);
		linkRepository.save(linkEntry);
		
		Map<String, Object> model = new HashMap<>();
		model.put("id", linkEntry.getId().toString().replaceAll("-", ""));
		model.put("file", file);

		return new ModelAndView("welcome", "model", model);
	}
	
	@RequestMapping("/download")
	public ModelAndView download(
			@RequestParam("file") String file, 
			@CookieValue(value = "authenticated", defaultValue = "false") String authCookie,
			@CookieValue(value = "username", defaultValue = "") String username) {
	
		//first, make sure we're logged in
		if (! authCookie.equals("true")) {
			return new ModelAndView(new RedirectView("login"));
		}
		
		UserEntry user = userRepository.findOne(username);
		if (user == null) {
			System.out.println("Failed to get user by username: " + username);
			return new ModelAndView(new RedirectView("login"));
		}
		
		//generate the link entry
		LinkEntry linkEntry = new LinkEntry(file, Constants.FileType.FILE_ARCHIVE, 1);
		linkRepository.save(linkEntry);
		
		Map<String, Object> model = new HashMap<>();
		model.put("id", linkEntry.getId().toString().replaceAll("-", ""));
		model.put("file", file);

		return new ModelAndView("download", "model", model);
	}
	
	@RequestMapping("/")
	public ModelAndView mainView(
			@CookieValue(value = "authenticated", defaultValue = "false") String authCookie,
			@CookieValue(value = "username", defaultValue = "") String username
			) {
 
		if (! authCookie.equals("true")) {
			return new ModelAndView(new RedirectView("login"));
		}
		
		UserEntry user = userRepository.findOne(username);
		if (user == null) {
			System.out.println("Failed to get user by username: " + username);
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
    		@CookieValue(value = "username", defaultValue = "") String username
    		) {
		System.out.println("Item uri: " + newItemUri + " " + downloadService.getMessage());
		
		UserEntry user = userRepository.findOne(username);
		if (user == null) {
			new ModelAndView("submit", "error", "Failed to get user by username: " + username);
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
		
		Cookie authCookie = new Cookie("authenticated", "true");
		authCookie.setMaxAge(3600*72);
		response.addCookie(authCookie);
		
		Cookie usernameCookie = new Cookie("username", userInfo.getUsername());
		usernameCookie.setMaxAge(3600*72);
		response.addCookie(usernameCookie);
		
		return mainView("true", userInfo.getUsername());
	}
}
