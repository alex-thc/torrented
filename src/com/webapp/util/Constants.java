package com.webapp.util;

public abstract class Constants {
	public static final String DOWNLOAD_BASE_PATH = "/data";
	public static final String ARCHIVE_BASE_PATH = "/archives";
	
	public static final int ARCHIVE_LINK_LIVES = 1; //use once
	public static final int VIDEO_LINK_LIVES = -1; //unlimited

	public enum UserGroup {
		GROUP_ADMIN, 
		GROUP_USER
	}
	
	public enum FileType {
		FILE_VIDEO,
		FILE_ARCHIVE
	}
}
