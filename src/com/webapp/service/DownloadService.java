package com.webapp.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.util.Util;
import com.webapp.exception.InvalidMagnetLinkException;
import com.webapp.service.entity.DownloadedItem;
import com.webapp.service.entity.UserEntry;
import com.webapp.service.repository.ItemRepository;
import com.webapp.service.repository.UserRepository;

import nl.stil4m.transmission.api.TransmissionRpcClient;
import nl.stil4m.transmission.api.domain.AddTorrentInfo;
import nl.stil4m.transmission.api.domain.AddedTorrentInfo;
import nl.stil4m.transmission.api.domain.File;
import nl.stil4m.transmission.api.domain.RemoveTorrentInfo;
import nl.stil4m.transmission.api.domain.TorrentInfo;
import nl.stil4m.transmission.api.domain.ids.Ids;
import nl.stil4m.transmission.rpc.RpcClient;
import nl.stil4m.transmission.rpc.RpcConfiguration;
import nl.stil4m.transmission.rpc.RpcException;

@Service
public class DownloadService {
		
	private static String host = "http://localhost:9091/transmission/rpc";
	private static String downloadDir = "/data";
	
	private static TransmissionRpcClient trClient;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private UserRepository userRepository;
	
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
	
	public void addItem(Item item, UserEntry user) throws RpcException {
		
		//get the hash of the item first
		String hash = null;
		if (com.webapp.util.Util.isMagnetLink(item.getUri())) {
			try {
				hash = com.webapp.util.Util.magnet2hash(item.getUri());
				
			} catch(InvalidMagnetLinkException ex) {
				throw new RpcException("Failed to get hash from the magnet link. Is it valid?");
			}
		}
		else {
			try {
				hash = com.webapp.util.Util.torrent2hash(item.getUri());
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RpcException("Failed to get hash from the torrent file: " + ex.getMessage());
			}
		}
		
		//System.out.println("HASH: " + hash);
		//if (1==1) return;
		
		//first, grant this user the access to the hash
		userRepository.grantAccessToHash(user, hash);
		
		//check if we have already downloaded the file
		//if so, just bump up the added date and get out
		DownloadedItem downloadedItem = itemRepository.findByHash(hash);
		if (downloadedItem != null) {
			itemRepository.resetAddedDate(downloadedItem, new Date()); //this will be overwritten if the download is active, but we don't care much about it
			return;
		}
		
		//try to download it
		AddTorrentInfo addTorrentInfo = new AddTorrentInfo();
		addTorrentInfo.setFilename(item.getUri());
		addTorrentInfo.setDownloadDir(downloadDir);
		addTorrentInfo.setPaused(false);
		//TODO: check for free space?
		AddedTorrentInfo addedInfo = trClient.addTorrent(addTorrentInfo);
	}
	
	public List<TorrentInfo> getAllItems() throws RpcException {
		List<TorrentInfo> torrents = trClient.getAllTorrentsInfo().getTorrents();
		//List<Item> items = new ArrayList<>();
		
		for(int i=0; i<torrents.size(); i++) {
			TorrentInfo info = torrents.get(i);
			
			System.out.println("Magnet: " + info.getMagnetLink());
			System.out.println("addedDate: " + new Date(info.getAddedDate()*1000));
			System.out.println("error: " + info.getError());
			System.out.println("errorString: " + info.getErrorString());
			System.out.println("eta: " + info.getError());
			System.out.println("id: " + info.getId());
			System.out.println("finished: " + info.getFinished());
			System.out.println("stalled: " + info.getStalled());
			System.out.println("magnet: " + info.getMagnetLink());
			System.out.println("name: " + info.getName());
			System.out.println("status: " + info.getStatus());
			System.out.println("totalSize: " + info.getTotalSize());
			System.out.println("webseedsSendingToUs: " + info.getWebseedsSendingToUs());
			
			for(File file : info.getFiles())
				System.out.println("torrent file: " + file.getName());
			
			System.out.println("----");
			
			//items.add(Item.fromTorrentInfo(info));
		}
		
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
	
	//thread for adding new items to the db after their download is finished
	public void checkFinishedDownloads() throws RpcException {
		List<TorrentInfo> torrents = trClient.getAllTorrentsInfo().getTorrents();
		
		for(int i=0; i<torrents.size(); i++) {
			TorrentInfo info = torrents.get(i);
			
			//check if an active record exists for this torrent and it's active status
			DownloadedItem storedItemStripped = itemRepository.getItemActiveFlagById(info.getId());
			
			//if it exists and not active - we have already updated it's status before
			//nothing to do - just move on to the next one
			if (storedItemStripped != null && storedItemStripped.isActive() == false) 
				continue;
			
			DownloadedItem item;
			
			if (info.getStatus() == 6 /*seeding*/ || info.getFinished()) {
				
				item = DownloadedItem.fromTorrentInfo(info, false);

			} else {
				item = DownloadedItem.fromTorrentInfo(info, true);				
			}
			
			//overwrite whatever we have in the database
			itemRepository.save(item); 
			
			//cleanup when we finished downloading and seeding
			if (info.getFinished()) {
				trClient.removeTorrent(new RemoveTorrentInfo(new Ids() {
					@Override
					public Object theObject() {
						return info.getId();
					}
				}, false));
			}
		}
	}
}
