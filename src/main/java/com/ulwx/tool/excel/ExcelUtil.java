package com.ulwx.tool.excel;

import com.ulwx.tool.excel.support.ExcelUtilGt2007;
import com.ulwx.tool.excel.support.ExcelUtilLt2007;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

	protected Logger logger = Logger.getLogger(ExcelUtil.class);
	private boolean greaterThan2007=true;
	public ExcelUtil(boolean greaterThan2007){
		this.greaterThan2007=greaterThan2007;
	}
	public ExcelUtil(){
		this.greaterThan2007=true;
	}
	public  InputStream getExcelStream(String sheetName, String title[], List datalist) {
		if(!greaterThan2007){
			return ExcelUtilLt2007.getExcelStream(sheetName,title,datalist);
		}else{
			return ExcelUtilGt2007.getExcelStream(sheetName,title,datalist);
		}
	}

	/**
	 * 写EXCEL文件
	 * @param path
	 * @param title
	 * @param datalist
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean writeExcel(String path, String[] title, List datalist) {
		if(!greaterThan2007){
			ExcelUtilLt2007 excelUtilLt2007=new ExcelUtilLt2007();
			return excelUtilLt2007.writeExcel(path,title,datalist);
		}else{
			ExcelUtilGt2007 excelUtilGt2007=new ExcelUtilGt2007();
			return excelUtilGt2007.writeExcel(path,title,datalist);
		}
	}

	@SuppressWarnings("unchecked")
	public List<List<Object>> readExcel(String path) {
		return readExcel(path, 0,Integer.MAX_VALUE);
	}
	@SuppressWarnings("unchecked")
	public List<List<Object>> readExcel(String path,int maxColumNum) {
		return readExcel(path, 0,maxColumNum);
	}
	/**
	 * 读EXCEL文件
	 *
	 * @param path
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<List<Object>> readExcel(String path, int numSheets,int maxColum) {
		if(!greaterThan2007){
			ExcelUtilLt2007 excelUtilLt2007=new ExcelUtilLt2007();
			return excelUtilLt2007.readExcel(path,numSheets,maxColum);
		}else{
			ExcelUtilGt2007 excelUtilGt2007=new ExcelUtilGt2007();
			return excelUtilGt2007.readExcel(path,numSheets,maxColum);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> readExcelByTitle(String path) {
		return readExcelByTitle(path, 0);
	}

	/**
	 * 读EXCEL文件，根据列名 返回值：包含HashMap的List
	 *
	 * @param path
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>>  readExcelByTitle(String path, int numSheets) {
		if(!greaterThan2007){
			ExcelUtilLt2007 excelUtilLt2007=new ExcelUtilLt2007();
			return excelUtilLt2007.readExcelByTitle(path,numSheets);
		}else{
			ExcelUtilGt2007 excelUtilGt2007=new ExcelUtilGt2007();
			return excelUtilGt2007.readExcelByTitle(path,numSheets);
		}
	}
}
