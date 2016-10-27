package com.webapp.service;

import java.net.URI;
import java.net.URISyntaxException;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.stil4m.transmission.api.TransmissionRpcClient;
import nl.stil4m.transmission.rpc.RpcClient;
import nl.stil4m.transmission.rpc.RpcConfiguration;
import nl.stil4m.transmission.rpc.RpcException;

public class DownloadService {
		
	private static String host = "http://localhost:9091/transmission/rpc";
	private static TransmissionRpcClient trClient;
	
	public DownloadService() {
		RpcConfiguration rpcConfig = new RpcConfiguration();
		try {
			rpcConfig.setHost(new URI(host));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RpcClient rpcClient = new RpcClient(
				rpcConfig, 
				new ObjectMapper());
		
		trClient = new TransmissionRpcClient(rpcClient);
		
	}
	
	public String getMessage() {
		try {
			return trClient.getSessionStats().getActiveTorrentCount().toString();
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Failed to get stats!";
		}
	}
}
