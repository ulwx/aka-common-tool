package com.ulwx.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {
	private static Logger log = LoggerFactory.getLogger(LogUtils.class);

	public static void info(String logID, String msg, String filePathName) {

		try {
			// .getResultSet(DataBase.java:1895):
			StackTraceElement[] straces = Thread.currentThread()
					.getStackTrace();
			String parentMethodLine = "[" + logID + "]["
					+ CTime.formatWholeAllDate() + "]";
			if (ArrayUtils.isNotEmpty(straces) && straces.length >= 3) {
				parentMethodLine = parentMethodLine + straces[2].getClassName()
						+ "." + straces[2].getMethodName() + "("
						+ straces[2].getFileName() + ":"
						+ straces[2].getLineNumber() + "):";
			}

			FileUtils.write(filePathName, parentMethodLine + msg + "\n", true,
					"utf-8");
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		for (int i = 0; i < 1; i++) {
			LogUtils.info("123", "ii" + i, "d:/data/" + i + ".txt");
		}

	}

}
