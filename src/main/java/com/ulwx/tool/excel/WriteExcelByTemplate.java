package com.ulwx.tool.excel;

import org.apache.log4j.Logger;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
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
			log.error("" + e);
			successTag = false;
		}
		return successTag;
	}
	
	/**
	 * 
	 * @param bean
	 * @param excelTemplateFilePath
	 * @param destExcelFilePath
	 * @param columnsToHide
	 * @return
	 */
	public static boolean exportColumnToHide(Map<String,Object> bean, 
			String excelTemplateFilePath, String destExcelFilePath,short[] columnsToHide){
		boolean successTag = true;



		try {
			JxlsHelper  jxlsHelper=JxlsHelper.getInstance();
			Context context = new Context(bean);

			InputStream inputStream = new FileInputStream(excelTemplateFilePath);
			OutputStream outStream = new FileOutputStream(destExcelFilePath);
			PoiTransformer transformer = PoiTransformer.createTransformer(inputStream,outStream);

			jxlsHelper.processTemplate( context,transformer);


		} catch (Exception e) {
			log.error("" + e);
			successTag = false;
		}
		return successTag;
	}
}
