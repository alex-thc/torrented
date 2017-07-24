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
public class ConvertService {
	@Autowired
	private ItemRepository itemRepository;
	
	private static final String BASE_PATH = Constants.DOWNLOAD_BASE_PATH;
	
	private Pattern handbrakeLogPattern = Pattern.compile("\\s([0-9]{1,2}\\.[0-9]{2})\\s%.*ETA\\s(\\w{9})");
	
	public void runConversionRound() {
		
		DownloadedItem item = itemRepository.getAndLockNonActiveItemToConvert();
		if (item == null) {
			//System.out.println("CONVERT: nothing to do");
			return;
		}
		
		List<String> convertedFiles = new ArrayList<>();
		List<String> tmp = new ArrayList<>();
		
		{
			String fileName = item.getFilesToConvert().get(0);
			
			System.out.println("CONVERT: converting " + fileName);
			
			String out = fileName + ".mp4";
			String convertCmd = String.format( //TODO: should we add -O here? (capital o)
				"/usr/bin/HandBrakeCLI -Z iPad -i \'%s\' -o \'%s\'",
				BASE_PATH + "/" + fileName, BASE_PATH + "/" + out);
			try {
				ShellCommand.executeCommand(convertCmd,"/tmp/handbrake.log." + item.getId());
				convertedFiles.add(out);
				tmp.add(fileName);
			} catch (ShellCommandException e) {
				System.out.println("CONVERT: " + e.getMessage());
				System.out.println("CONVERT: " + e.getCmdLine());
				System.out.println("CONVERT: " + e.getBufOutput());
			}
		}
		
		if (! convertedFiles.isEmpty()) {
			if (item.getVideoFiles() == null)
				item.setVideoFiles(new ArrayList<String>());
			item.getVideoFiles().addAll(convertedFiles);
		}
		item.getFilesToConvert().removeAll(tmp);
		if (item.getFilesToConvert().isEmpty()) //nullify the array so that we don't look at this doc again
			item.setFilesToConvert(null);
		item.setProcessing(false);
		itemRepository.save(item);
	}
	
	public void updateProcessingPercentageComplete() {
		
		List<DownloadedItem> items = itemRepository.getItemsInProcess();
		
		if (items == null)
			return;
		
		for(DownloadedItem item : items) {
			
			//XXX: this is hacky
			if (item.getProcessingStatus() == "Archiving")
				continue;
			
			String fileName = "/tmp/handbrake.log." + item.getId();
			String lastLine = com.webapp.util.Util.tail(fileName);
			Matcher m = handbrakeLogPattern.matcher(lastLine);
			String processingStatus = null;
			if (m.find() && m.groupCount() == 2) {
				processingStatus = String.format("%s%%, eta %s", m.group(1), m.group(2));
			} else {
				//System.out.println("CONVERT: failed to process the last line of " + fileName);
			}
			itemRepository.setItemProcessingStatus(item, processingStatus);
		}
	}
}
