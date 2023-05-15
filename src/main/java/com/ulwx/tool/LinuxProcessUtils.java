package com.ulwx.tool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinuxProcessUtils {

	static Logger log = LoggerFactory.getLogger(LinuxProcessUtils.class);

	/**
	 * 
	 * @param processInfoStr
	 *            唯一标示查找进程的字符串
	 * @return
	 */
	public static Map<Integer, String> lookProcessId(String processInfoStr) {

		ProcessBuilder builder = null;
		InputStream input = null;
		String pid = "";
		Map<Integer, String> map = new TreeMap<Integer, String>();
		try {
			builder = new ProcessBuilder("/bin/bash", "-c", "ps aux");

			Process p = builder.start();
			// in.close();
			input = p.getInputStream();
			String result = IOUtils.toString(input, "iso-8859-1", false);
			// log.info("result:\n" + result);
			// root 8084 0.3 1.5 1265636 65564 ? Sl 17:06
			TimeUnit.SECONDS.sleep(5);

			Pattern pa = Pattern.compile("root\\s+(\\d+)\\s+.*"
					+ processInfoStr + ".*");

			Matcher m = pa.matcher(result);
			// System.out.println("######################");

			while (m.find()) {
				String group = m.group();
				pid = m.group(1);
				map.put(Integer.valueOf(pid), group);
			}
		} catch (Exception e) {

		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
		}
		return map;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
