package com.webapp.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapp.exception.ShellCommandException;
import com.webapp.service.entity.DownloadedItem;
import com.webapp.service.repository.ItemRepository;
import com.webapp.util.Constants;
import com.webapp.util.ShellCommand;
import com.webapp.util.Util;

@Service
public class CleanupService {
	@Autowired
	private ItemRepository itemRepository;
		
	private static final Integer EXPIRY_DAYS = 14;
	
	public void runCleanupRound() {
		Calendar calendar = Calendar.getInstance(); // this would default to now
		calendar.add(Calendar.DAY_OF_MONTH, -EXPIRY_DAYS);
		List<DownloadedItem> itemsToCleanup = itemRepository.getNonActiveItemsAddedBeforeDate(calendar.getTime());
				
		for (DownloadedItem item : itemsToCleanup) {
			System.out.println("CLEANUP: cleaning up " + item.getName());
			
			//clean up the archive
			if (item.getArchiveFile() != null) {
				Util.removeFromDisk(Constants.ARCHIVE_BASE_PATH, item.getArchiveFile());
			}
			
			//clean up everything else
			Set<String> filesToDelete = new HashSet<>();
			if (item.getDownloadedFiles() != null)
				filesToDelete.addAll(item.getDownloadedFiles());
			if (item.getVideoFiles() != null)
				filesToDelete.addAll(item.getVideoFiles());
			
			itemRepository.delete(item);
			//TODO: remove the hash from users access lists
			
			if (filesToDelete.isEmpty())
				continue;
			
			String dir = ShellCommand.getBaseDirPath(filesToDelete.iterator().next());
			if (dir != null) //we can clean up the directory
			{
				Util.removeFromDisk(Constants.DOWNLOAD_BASE_PATH, dir);
//				String removeCmd = String.format("/usr/bin/rm -rf \'%s\'", BASE_PATH + "/" + dir);
//				System.out.println("CLEANUP: removing " + dir + " : " + removeCmd);
//				
//				try {
//					ShellCommand.executeCommand(removeCmd, "/tmp/cleanup.log");
//				} catch (ShellCommandException e) {
//					System.out.println("CLEANUP: " + e.getMessage());
//					System.out.println("CLEANUP: " + e.getCmdLine());
//					System.out.println("CLEANUP: " + e.getBufOutput());
//				}
				
			} else { //oops, seems like the data is in our root, so removing files 1 by 1
				for (String file : filesToDelete) {
					Util.removeFromDisk(Constants.DOWNLOAD_BASE_PATH, file);
//					String removeCmd = String.format("/usr/bin/rm -f \'%s\'", BASE_PATH + "/" + file);
//					System.out.println("CLEANUP: removing " + file + " : " + removeCmd);
//					
//					try {
//						ShellCommand.executeCommand(removeCmd, "/tmp/cleanup.log");
//					} catch (ShellCommandException e) {
//						System.out.println("CLEANUP: " + e.getMessage());
//						System.out.println("CLEANUP: " + e.getCmdLine());
//						System.out.println("CLEANUP: " + e.getBufOutput());
//					}
				}
			}
			
		}
	}
}
