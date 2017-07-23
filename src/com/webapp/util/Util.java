package com.webapp.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.webapp.exception.InvalidMagnetLinkException;

public abstract class Util {
	
	/**
	 * 
	 * @param file
	 * @return the last line until \r or \r\n is met
	 */
	public static String tail( String file ) {
	    RandomAccessFile fileHandler = null;
	    try {
	        fileHandler = new RandomAccessFile( file, "r" );
	        long fileLength = fileHandler.length() - 1;
	        StringBuilder sb = new StringBuilder();

	        for(long filePointer = fileLength; filePointer != -1; filePointer--){
	            fileHandler.seek( filePointer );
	            int readByte = fileHandler.readByte();

	            if( readByte == 0xA ) {
	                if( filePointer == fileLength ) {
	                    continue;
	                }
	                break;

	            } else if( readByte == 0xD ) {
	                if( filePointer == fileLength - 1 ) {
	                    continue;
	                }
	                break;
	            }

	            sb.append( ( char ) readByte );
	        }

	        String lastLine = sb.reverse().toString();
	        return lastLine;
	    } catch( java.io.FileNotFoundException e ) {
	        e.printStackTrace();
	        return null;
	    } catch( java.io.IOException e ) {
	        e.printStackTrace();
	        return null;
	    } finally {
	        if (fileHandler != null )
	            try {
	                fileHandler.close();
	            } catch (IOException e) {
	                /* ignore */
	            }
	    }
	}
	
	public static final Pattern magnet_check_regex = Pattern.compile("^magnet:");
	public static final Pattern magnet_hash_regex = Pattern.compile("([A-Z0-9]{40})");
	
	public static boolean isMagnetLink(String uri) {
		Matcher matcher = magnet_check_regex.matcher(uri);
		return matcher.find();
	}
	
    public static String magnet2hash(String magnet) throws InvalidMagnetLinkException {
        String magnet1 = magnet.toUpperCase();
        Matcher matcher = magnet_hash_regex.matcher(magnet1);
        if (!matcher.find()) {
            throw new InvalidMagnetLinkException("The magnet link \"" + magnet + "\" isn't valid.");
        }
        String hash = matcher.group(1);
        return hash;
    }
}
