package com.webapp.service;

import java.io.BufferedReader;
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
	private static final String OUTPUT_PATH = "convert";
	
	public void runConversionRound() {
		
		DownloadedItem item = itemRepository.getAndLockItemToConvert();
		if (item == null) {
			//System.out.println("CONVERT: nothing to do");
			return;
		}
		
		List<String> convertedFiles = new ArrayList<>();
		
		for(String fileName : item.getFilesToConvert()) {
			System.out.println("CONVERT: converting " + fileName);
			
			String out = OUTPUT_PATH + "/" + fileName + ".mp4";
			String convertCmd = String.format( //TODO: should we add -O here? (capital o)
				"HandBrakeCLI -Z iPad -i %s -o %s 2>&1",
				BASE_PATH + "/" + fileName, BASE_PATH + "/" + out);
			try {
				executeCommand(convertCmd);
				convertedFiles.add(out);
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
		item.setFilesToConvert(null);
		item.setProcessing(false);
		itemRepository.save(item);
	}
	
	private String executeCommand(String command) throws ShellCommandException {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new ShellCommandException(e.getMessage());
		}
		
		if (p.exitValue() != 0) //process failed
			throw new ShellCommandException("Process failed: " + p.exitValue(), command, output.toString());
			
		return output.toString();

	}
}
