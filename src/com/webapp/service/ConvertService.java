package com.webapp.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapp.exception.ShellCommandException;
import com.webapp.service.entity.DownloadedItem;
import com.webapp.service.repository.ItemRepository;

@Service
public class ConvertService {
	@Autowired
	private ItemRepository itemRepository;
	
	private static final String BASE_PATH = "/data";
	
	public void runConversionRound() {
		
		DownloadedItem item = itemRepository.getAndLockItemToConvert();
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
				executeCommand(convertCmd,"/tmp/handbrake.log");
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
	
	private String executeCommand(String command, String logFile) throws ShellCommandException {

		StringBuffer output = new StringBuffer();
		
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
		builder.redirectOutput(new File(logFile));
		builder.redirectError(new File(logFile));
		Process p;

		try {
			p = builder.start();
			p.waitFor();

		} catch (Exception e) {
			e.printStackTrace();
			throw new ShellCommandException(e.getMessage());
		}
		
		if (p.exitValue() != 0) //process failed
			throw new ShellCommandException("Process failed: " + p.exitValue(), command, output.toString());
			
		return output.toString();

	}
}
