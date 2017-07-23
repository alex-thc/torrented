package com.webapp.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

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
    
    public static String torrent2hash(String torrentUrl) throws IOException, NoSuchAlgorithmException {
    	File file = new File("/tmp/" + UUID.randomUUID().toString());

    	//generate a file that doesn't exist
        while (file.exists()) {
        	file = new File("/tmp/" + UUID.randomUUID().toString());
        }
        
        //get the torrent file
        URL url = new URL(torrentUrl);
        FileUtils.copyURLToFile(url, file);
        //System.out.println("[*] Done [Saved to \"" + file.getName() + "\"]");
        
        //get the sha1
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        InputStream input = null;

        try {
            input = new FileInputStream(file);
            StringBuilder builder = new StringBuilder();
            while (!builder.toString().endsWith("4:info")) {
                builder.append((char) input.read()); // It's ASCII anyway.
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            for (int data; (data = input.read()) > -1; output.write(data));
            sha1.update(output.toByteArray(), 0, output.size() - 1);
        } finally {
            if (input != null) try { input.close(); } catch (IOException ignore) {}
        }

        byte[] hash = sha1.digest();
        
        //convert the bytes of the hash into a string
        StringBuffer hexString = new StringBuffer();
        
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0"
                        + Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        
        FileUtils.forceDelete(file);
        
        return hexString.toString().toUpperCase();
    }
}
