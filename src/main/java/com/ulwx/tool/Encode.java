package com.ulwx.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Encode {      
	static Logger log = LoggerFactory.getLogger(Encode.class);

	/**
	 * 用于网页里的转移字符
	 * 
	 * @param gbString
	 * @return
	 */
	public static String gb2unicode(final String gbString) {
		if (gbString == null) {
			return "";
		}
		char[] utfBytes = gbString.toCharArray();
		String unicodeBytes = "";
		for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
			if (utfBytes[byteIndex] >= '!' && utfBytes[byteIndex] <= '~') {
				unicodeBytes += utfBytes[byteIndex];
			} else {
				String hexB = Integer.toHexString(utfBytes[byteIndex]);
				if (hexB.length() <= 2) {
					hexB = "00" + hexB;
				}
				unicodeBytes = unicodeBytes + "&#x" + hexB + ";";
			}
		}
		return unicodeBytes;
	}

	public static String unicode2gb(String strValue) {

		if (strValue == null || strValue.trim().length() == 0) {
			return "";
		}
		Matcher m = Pattern.compile("&?#[xX](\\w{1,4});?").matcher(strValue);

		StringBuffer sbuf = new StringBuffer();

		// perform the replacements:
		while (m.find()) {
			String value = m.group(1);
			int l = Integer.valueOf(value, 16).intValue();
			char c = (char)(0x0ffff & l);
			m.appendReplacement(sbuf, c + "");
		}
		// Put in the remainder of the text:
		m.appendTail(sbuf);

		String str = sbuf.toString();
		Matcher m2 = Pattern.compile("&?#(\\d+);?").matcher(str);
		sbuf.setLength(0);
		while (m2.find()) {
			String value = m2.group(1);
			int l = Integer.valueOf(value, 10).intValue();

			char c = (char)(0x0ffff & l);
			m2.appendReplacement(sbuf, c + "");
		}
		m2.appendTail(sbuf);
		return sbuf.toString();
		// return EscapeUtil.unescapeHtml(strValue);
	}

	public static String getUTF8BytesStream(String s) {
		try {
			byte[] bs = s.getBytes("utf-8");
			String r = new String(bs, "iso-8859-1");
			return r;
		} catch (Exception e) {
			log.error("得到utf-8字节流出错！");
		}
		return null;
	}

	public static byte[] encode(byte[] src, String srcCharset, String desCharset) {
		try {
			String s = new String(src, srcCharset);
			return s.getBytes(desCharset);
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

	public static String getGBKBytesStream(String s) {
		try {
			byte[] bs = s.getBytes("GBK");
			String r = new String(bs, "iso-8859-1");
			return r;
		} catch (Exception e) {
			log.error("得到utf-8字节流出错！");
		}
		return null;
	}

	public static String GBKBytes2String(String s) {
		try {
			byte[] bs = s.getBytes("iso-8859-1");
			String r = new String(bs, "gbk");
			return r;
		} catch (Exception e) {
			log.error(" 转化编码出错！");
		}
		return null;
	}

	public static boolean charsetIsSupported(String charset) {
		return org.apache.commons.lang3.CharEncoding.isSupported(charset);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {

			String s = "ttyy&#x4e2d;54;dd  &#x56fd;&#82;&nbsp;&dd";
			System.out.println(Encode.unicode2gb(s));

			byte[] bs = new byte[] { 0x22, 0x20 };
			System.out.println(new String(bs, "gbk"));
			//
		} catch (Exception se) {
			se.printStackTrace();
		}

	}

}
