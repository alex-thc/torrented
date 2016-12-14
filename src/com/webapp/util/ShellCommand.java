package com.webapp.util;

import java.io.File;

import com.webapp.exception.ShellCommandException;

public abstract class ShellCommand {
	
	public static String executeCommand(String command, String logFile) throws ShellCommandException {

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
