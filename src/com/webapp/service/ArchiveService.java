package com.webapp.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapp.exception.ShellCommandException;
import com.webapp.service.entity.DownloadedItem;
import com.webapp.service.repository.ItemRepository;
import com.webapp.util.Constants;
import com.webapp.util.ShellCommand;
import com.webapp.util.Util;

@Service
public class ArchiveService {
	@Autowired
	private ItemRepository itemRepository;
	
	private static final String BASE_PATH = Constants.DOWNLOAD_BASE_PATH;
	
	public String prepareArchiveCmd(List<String> filesToAdd, String outFileName) {
		String filesString = "";
		for (String file : filesToAdd) filesString = filesString + file + " ";
		
		return "tar -zcvf " + Constants.ARCHIVE_BASE_PATH + "/" + outFileName 
				+ " -C " + BASE_PATH + " " + filesString;
	}
	
	public void runArchiveRound() {
		
		DownloadedItem item = itemRepository.getAndLockNonActiveItemToArchive();
		if (item == null || item.getDownloadedFiles() == null || item.getDownloadedFiles().isEmpty()) {
			//System.out.println("CONVERT: nothing to do");
			return;
		}
		
		//update processing status so that everyone knows whatsup
		itemRepository.setItemProcessingStatus(item, "Archiving");
		
		String out = item.getHash() + ".tgz";
		
		String archiveCmd = prepareArchiveCmd(item.getDownloadedFiles(), out);
		
		try {
			ShellCommand.executeCommand(archiveCmd,"/tmp/archive.log." + item.getId());
			item.setArchiveFile(out);
			item.setArchiveError(null);
		} catch (ShellCommandException e) {
			System.out.println("CONVERT: " + e.getMessage());
			System.out.println("CONVERT: " + e.getCmdLine());
			System.out.println("CONVERT: " + e.getBufOutput());
			
			item.setArchiveFile(null);
			item.setArchiveError(e.getBufOutput());
		}
		
		item.setProcessingStatus("");
		item.setProcessing(false);
		itemRepository.save(item);
	}

}
