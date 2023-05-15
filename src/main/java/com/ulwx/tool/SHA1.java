package com.ulwx.tool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SHA1算法 2010-8-4上午11:41:05 SHA1
 * 
 * @author lamfire
 */
public class SHA1 {
	private static Logger logger = LoggerFactory.getLogger(SHA1.class);

	private static MessageDigest MD = null;

	public static byte[] digest(byte[] datas) {
		return getMessageDigest().digest(datas);
	}

	public static MessageDigest getMessageDigest() {
		if (MD == null) {
			try {
				MD = MessageDigest.getInstance("SHA-1");
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}
		return MD;
	}
	
	public static String digest(String str){
		byte[] bs=digest(str.getBytes());
		String buf = ByteUtils.toHexAscii(bs, "");
		return buf;
		
		
	}
}
