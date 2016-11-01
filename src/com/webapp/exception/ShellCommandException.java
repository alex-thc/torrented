package com.webapp.exception;

public class ShellCommandException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8916402109966410129L;

	private String cmdLine;
	private String bufOutput;
	
	public ShellCommandException(String message, String cmdLine, String bufOutput) {
		super(message);
		this.bufOutput = bufOutput;
		this.setCmdLine(cmdLine);
	}
	
	public ShellCommandException(String message) {
		super(message);
	}
	
	public String getBufOutput() {
		return bufOutput;
	}

	public String getCmdLine() {
		return cmdLine;
	}

	public void setCmdLine(String cmdLine) {
		this.cmdLine = cmdLine;
	}
}
