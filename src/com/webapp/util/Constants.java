package com.webapp.util;

public abstract class Constants {
	public static final String DOWNLOAD_BASE_PATH = "/data";
	public static final String ARCHIVE_BASE_PATH = "/archives";
	
	public static final int FILE_LINK_LIVES = 1; //use once
	public static final int VIDEO_LINK_LIVES = -1; //unlimited
	
	public static final int NEW_ITEMS_PER_DAY_LIMIT = 4;
	public static final int NEW_LINKS_PER_DAY_LIMIT = 10; 
	
	public static final long AVAIL_SPACE_LIMIT_BYTES = 10*1024*1024*1024; //10GB

	public enum UserGroup {
		GROUP_ADMIN, 
		GROUP_USER
	}
	
	public enum FileType {
		FILE_VIDEO,
		FILE_ARCHIVE,
		FILE_REGULAR
	}
	
	public enum ActivityType {
		SUBMIT_NEW_ITEM
	}
}
