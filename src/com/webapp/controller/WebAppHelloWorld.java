package com.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.Ternary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.webapp.service.DownloadService;
import com.webapp.service.Item;
import com.webapp.service.LoginInfo;
import com.webapp.service.entity.DownloadedItem;
import com.webapp.service.entity.LinkEntry;
import com.webapp.service.repository.ItemRepository;
import com.webapp.service.repository.LinkRepository;

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
	
	@RequestMapping("/welcome")
	public ModelAndView helloWorld(@RequestParam("file") String file) {
	
		LinkEntry linkEntry = new LinkEntry(file);
		linkRepository.save(linkEntry);
				
		return new ModelAndView("welcome", "id", linkEntry.getId().toString().replaceAll("-", ""));
	}
	
	@RequestMapping("/")
	public ModelAndView mainView() {
 
		ModelAndView model = new ModelAndView("index");
		
		List<TorrentInfo> activeItems = null;
		
		try {
			activeItems = downloadService.getAllItems();
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addObject("activeItemsList", activeItems);
		
		List<DownloadedItem> downloadedItems = itemRepository.findAll();
		model.addObject("downloadedItemsList", downloadedItems);
		
		return model;
	}
	
	@RequestMapping(value="/submit", method=RequestMethod.POST)
    public ModelAndView itemSubmit(@RequestParam("newItemUri") String newItemUri) {
		System.out.println("Item uri: " + newItemUri + " " + downloadService.getMessage());
		
		try {
			downloadService.addItem(new Item(newItemUri));
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
/*		ModelAndView model= null;
		try
		{
			boolean isValidUser = loginDelegate.isValidUser(loginBean.getUsername(), loginBean.getPassword());
			if(isValidUser)
			{
				System.out.println("User Login Successful");
				request.setAttribute("loggedInUser", loginBean.getUsername());
				model = new ModelAndView("welcome");
			}
			else
			{
				model = new ModelAndView("login");
model.addObject("loginBean", loginBean);
				request.setAttribute("message", "Invalid credentials!!");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return model;*/
		return mainView();
	}
}
