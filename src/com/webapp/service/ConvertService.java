package com.webapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import nl.stil4m.transmission.rpc.RpcException;

public class ConvertService {
	@Autowired
	private DownloadService downloadService;
	
	public void runConversionRound() {
		System.out.println("CONVERT!");
		
		List<Item> items = null;
		
		try {
			items = downloadService.getAllItems();
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
