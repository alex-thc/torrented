package com.webapp.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapp.exception.ShellCommandException;
import com.webapp.service.entity.DownloadedItem;
import com.webapp.service.repository.ItemRepository;
import com.webapp.util.ShellCommand;

@Service
public class CleanupService {
	@Autowired
	private ItemRepository itemRepository;
	
	private static final String BASE_PATH = "/data";
	
	private static final Integer EXPIRY_DAYS = 30;
	
	public void runCleanupRound() {
		Calendar calendar = Calendar.getInstance(); // this would default to now
		calendar.add(Calendar.DAY_OF_MONTH, -EXPIRY_DAYS);
		List<DownloadedItem> itemsToCleanup = itemRepository.getItemsAddedBeforeDate(calendar.getTime());
		
		for (DownloadedItem item : itemsToCleanup) {
			System.out.println("CLEANUP: cleaning up " + item.getName());
			
			List<String> filesToDelete = new ArrayList<>();
			filesToDelete.addAll(item.getDownloadedFiles());
			filesToDelete.addAll(item.getConvertedFiles());
			
			itemRepository.delete(item);
			
			for (String file : filesToDelete) {
				String removeCmd = String.format("/usr/bin/rm \'%s\'", BASE_PATH + "/" + file);
				try {
					ShellCommand.executeCommand(removeCmd, "/tmp/cleanup.log");
				} catch (ShellCommandException e) {
					System.out.println("CLEANUP: " + e.getMessage());
					System.out.println("CLEANUP: " + e.getCmdLine());
					System.out.println("CLEANUP: " + e.getBufOutput());
				}
			}
		}
	}
}
