package com.ulwx.tool;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MD5 implements java.io.Serializable {
	private static Logger log = LoggerFactory.getLogger(MD5.class);

	private static String to32BitString(String plainText, boolean is32or16,
			String charset) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (StringUtils.hasText(charset))
				md.update(plainText.getBytes(charset));
			else {
				md.update(plainText.getBytes());
			}
			byte[] b = md.digest();
			String buf = ByteUtils.toHexAscii(b, "");

			if (is32or16) {
				return buf;
			} else {
				return buf.substring(8, 24);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("", e);
		}
		return "";
	}

	/**
	 * 产生32位md5加密字符串
	 * 
	 * @param s
	 * @return
	 */
	public final static String MD5generator(String s) {
		// String charset =System.getProperties()
		return MD5.to32BitString(s, true, "");
	}

	/**
	 * 产生32位md5加密字符串
	 * 
	 * @param s
	 * @return
	 */
	public final static String MD5generator(String s, String charset) {
		return MD5.to32BitString(s, true, charset);
	}

	/**
	 * 产生16为md5加密字符串
	 * 
	 * @param s
	 * @return
	 */
	public final static String MD5generator16Bit(String s, String charset) {
		return MD5.to32BitString(s, false, charset);
	}

	public final static String MD5generator16Bit(String s) {
		return MD5.to32BitString(s, false, "");
	}

	public final static String MD5generator16Bit(byte[] bs) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bs);
			byte[] b = md.digest();
			String buf = ByteUtils.toHexAscii(b, "");
			return buf.substring(8, 24);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("", e);
		}
		return "";
	}

    public static byte[] digest(byte data[])
    {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] b = md.digest(data);
			return b;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("", e);
		}
        return null;
    }
	public static String MD5SecureGenerator16Bit(String s) {
		String salt = "xe@~!&#45%#%g_";
		String ret = MD5generator(s);
		return MD5generator(salt + ret);
	}

	public static String MD5SecureGenerator(String s) {
		return MD5SecureGenerator16Bit(s);
	}

	public static void main(String args[]) {

		// MD5 m = new MD5();
		//
		// String ss ="128016128016";
		// //String s1=MD5.toString(ss);
		// String s2 =MD5.MD5generator(ss);
		// System.out.println(s2);
		// log.debug("ddddd");

		System.out.println(MD5SecureGenerator("123456"));
		

	}

}


