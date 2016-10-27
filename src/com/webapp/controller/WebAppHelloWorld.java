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

@Controller
public class WebAppHelloWorld {
	
	@Autowired
	DownloadService downloadService;
	
	@RequestMapping("/welcome")
	public ModelAndView helloWorld() {
 		
		String message = "<br><div style='text-align:center;'>"
				+ "<h3>********** Hello World, Spring MVC Tutorial</h3>This message is coming from CrunchifyHelloWorld.java **********</div><br><br>";
		return new ModelAndView("welcome", "message", message);
	}
	
	@RequestMapping("/")
	public ModelAndView mainView() {
 
		ModelAndView model = new ModelAndView("index");
		
		List<Item> items = new ArrayList<>();
		items.add(new Item("1","Done"));
		items.add(new Item("2","In progress"));
		
		model.addObject("itemsList", items);
		
		//model.addObject("newItem", new Item());
		
		return model;
	}
	
	@RequestMapping(value="/submit", method=RequestMethod.POST)
    public String itemSubmit(@RequestParam("newItemUri") String newItemUri) {
		System.out.println("Item uri: " + newItemUri + " " + downloadService.getMessage());
        return "submit";
    }

}
