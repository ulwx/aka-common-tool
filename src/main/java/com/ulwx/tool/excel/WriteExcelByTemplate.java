package com.ulwx.tool.excel;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.poi.PoiContext;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
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
		return WriteExcelByTemplate.export(bean, inputStream, outStream);
		} catch (Exception e) {
			log.error("" + e,e);
			successTag = false;
		}
		return successTag;
	}
	public static boolean export(Map<String,Object> bean,
								 InputStream inputStream, OutputStream outStream){
		Workbook book = null;
		PoiTransformer  transformer = null;
		Context context = null;
		int rowAccessWindowSize=500;
		boolean successTag = true;
		try {
			book = WorkbookFactory.create(inputStream);
			transformer = PoiTransformer.createSxssfTransformer(book);
			AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);
			List<Area> xlsAreaList = areaBuilder.build();
			//5.因为模板里面只有一个所以这个直接get 0
			Area xlsArea = xlsAreaList.get(0);
			context = new PoiContext(bean);
			xlsArea.applyAt(new CellRef("Result!A1"), context);
			context.getConfig().setIsFormulaProcessingRequired(false);
			book.removeSheetAt(book.getSheetIndex(xlsArea.getStartCellRef().getSheetName()));
			book.setForceFormulaRecalculation(true);
			transformer.getWorkbook().write(outStream);

		} catch (Exception e) {
			log.error("" + e,e);
			successTag = false;
		}
		return successTag;
	}

}
