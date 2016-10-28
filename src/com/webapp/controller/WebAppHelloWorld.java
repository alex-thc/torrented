package com.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.webapp.service.DownloadService;
import com.webapp.service.Item;
import com.webapp.service.LinkTable;
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
	private LinkTable linkTable;
	
	@RequestMapping("/welcome")
	public ModelAndView helloWorld() {
 		
		String link = "test.mp4";
		Long id = linkTable.getLink(link); 
				
		return new ModelAndView("welcome", "id", id);
	}
	
	@RequestMapping("/")
	public ModelAndView mainView() {
 
		ModelAndView model = new ModelAndView("index");
		
		List<TorrentInfo> items = null;
		
		try {
			items = downloadService.getAllItems();
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addObject("itemsList", items);
		
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

}
