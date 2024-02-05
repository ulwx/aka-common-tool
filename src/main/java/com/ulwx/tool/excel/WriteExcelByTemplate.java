package com.ulwx.tool.excel;

import org.apache.log4j.Logger;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class WriteExcelByTemplate {
	
	private static final Logger log = Logger.getLogger(WriteExcelByTemplate.class);
	public static boolean export(Map<String,Object> bean,
			String excelTemplateFilePath, String destExcelFilePath){
		boolean successTag = true;
		try {
		Context context = new Context(bean);
		InputStream inputStream = new FileInputStream(excelTemplateFilePath);
		OutputStream outStream = new FileOutputStream(destExcelFilePath);
		JxlsHelper.getInstance().processTemplate(inputStream, outStream, context);
		} catch (Exception e) {
			log.error("" + e,e);
			successTag = false;
		}
		return successTag;
	}
	public static boolean export(Map<String,Object> bean,
								 InputStream inputStream, OutputStream outStream){
		boolean successTag = true;
		try {
			Context context = new Context(bean);
			JxlsHelper.getInstance().processTemplate(inputStream, outStream, context);
		} catch (Exception e) {
			log.error("" + e,e);
			successTag = false;
		}
		return successTag;
	}

}
