package com.webapp.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.stil4m.transmission.api.TransmissionRpcClient;
import nl.stil4m.transmission.api.domain.AddTorrentInfo;
import nl.stil4m.transmission.api.domain.AddedTorrentInfo;
import nl.stil4m.transmission.api.domain.TorrentInfo;
import nl.stil4m.transmission.api.domain.TorrentInfoCollection;
import nl.stil4m.transmission.rpc.RpcClient;
import nl.stil4m.transmission.rpc.RpcConfiguration;
import nl.stil4m.transmission.rpc.RpcException;

public class DownloadService {
		
	private static String host = "http://localhost:9091/transmission/rpc";
	private static String downloadDir = "/tmp";
	
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
	
	public void addItem(Item item) throws RpcException {
		AddTorrentInfo addTorrentInfo = new AddTorrentInfo();
		addTorrentInfo.setFilename(item.getUri());
		addTorrentInfo.setDownloadDir(downloadDir);
		
		AddedTorrentInfo addedInfo = trClient.addTorrent(addTorrentInfo);
		
		//TODO: store torrent information (new store class and new entity class)
	}
	
	public List<TorrentInfo> getAllItems() throws RpcException {
		List<TorrentInfo> torrents = trClient.getAllTorrentsInfo().getTorrents();
		
//		for(int i=0; i<torrents.size(); i++) {
//			TorrentInfo info = torrents.get(i);
//			
//			System.out.println("Magnet: " + info.getMagnetLink());
//			System.out.println("addedDate: " + new Date(info.getAddedDate()*1000));
//			System.out.println("error: " + info.getError());
//			System.out.println("errorString: " + info.getErrorString());
//			System.out.println("eta: " + info.getError());
//			System.out.println("id: " + info.getId());
//			System.out.println("finished: " + info.getFinished());
//			System.out.println("stalled: " + info.getStalled());
//			System.out.println("magnet: " + info.getMagnetLink());
//			System.out.println("name: " + info.getName());
//			System.out.println("status: " + info.getStatus());
//			System.out.println("totalSize: " + info.getTotalSize());
//			System.out.println("webseedsSendingToUs: " + info.getWebseedsSendingToUs());
//			
//			System.out.println("----");
//		}
		
		return torrents;
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
