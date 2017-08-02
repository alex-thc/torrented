package com.webapp.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
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
		
		//return "tar -zcvf " + Constants.ARCHIVE_BASE_PATH + "/" + outFileName 
		//		+ " -C " + BASE_PATH + " " + filesString;
		return "cd " + BASE_PATH + " && " + 
				"zip -0 -r " + Constants.ARCHIVE_BASE_PATH + "/" + outFileName +
				" " + filesString;
	}
	
	public void runArchiveRound() {
		
		//check free space
		try {
			if (! Util.checkSpaceAvailable(Constants.ARCHIVE_BASE_PATH))
			{
				System.out.println("Skipping archive round because not enough disk"
						+ " space is available: " + Constants.ARCHIVE_BASE_PATH);
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		DownloadedItem item = itemRepository.getAndLockNonActiveItemToArchive(); //this will also update processing status so that everyone knows whatsup
		if (item == null) {
			//System.out.println("CONVERT: nothing to do");
			return;
		}
		
		if (item.getDownloadedFiles() != null && ! item.getDownloadedFiles().isEmpty())
		{
			String out = item.getHash() + ".zip";
			
			String archiveCmd = prepareArchiveCmd(item.getDownloadedFiles(), out);
			
			try {
				ShellCommand.executeCommand(archiveCmd,"/tmp/archive.log." + item.getId());
				item.setArchiveFile(out);
				item.setArchiveError(null);
			} catch (ShellCommandException e) {
				System.out.println("ARCHIVE: " + e.getMessage());
				System.out.println("ARCHIVE: " + e.getCmdLine());
				System.out.println("ARCHIVE: " + e.getBufOutput());
				
				item.setArchiveFile(null);
				item.setArchiveError(e.getMessage());
			}
		}
		
		item.setProcessingStatus("");
		item.setProcessing(false);
		itemRepository.save(item);
	}

}
