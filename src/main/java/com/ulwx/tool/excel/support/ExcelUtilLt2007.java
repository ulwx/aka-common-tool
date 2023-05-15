package com.ulwx.tool.excel.support;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class ExcelUtilLt2007 {

	protected Logger logger = Logger.getLogger(ExcelUtilLt2007.class);

	public static InputStream getExcelStream(String sheetName, String title[], List datalist) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet(sheetName);
			HSSFRow[] row = new HSSFRow[datalist.size() + 1];// 行
			HSSFCell[] valueCell = new HSSFCell[title.length];// 列
			row[0] = sheet.createRow(0);
			// 创建列头
			HSSFCell[] headerCell = new HSSFCell[title.length];
			for (int i = 0; i < title.length; i++) {
				headerCell[i] = row[0].createCell(i);
				headerCell[i].setCellValue(title[i]);
			}
			// 往列中写数据
			Iterator iterator = datalist.iterator();
			int n_row = 1;
			while (iterator.hasNext()) {
				String data[] = (String[])iterator.next();
				row[n_row] = sheet.createRow(n_row);
				for (int n_cel = 0; n_cel < data.length; n_cel++) {
					valueCell[n_cel] = row[n_row].createCell(n_cel);
					try {
						int x = Integer.parseInt(data[n_cel]);
						valueCell[n_cel].setCellValue(x);
					} catch (Exception e) {
						valueCell[n_cel].setCellValue(data[n_cel]);
					}
				}
				n_row++;
			}
			sheet.setPrintGridlines(false);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			wb.write(os);
			os.flush();
			os.close();
			byte[] buf = os.toByteArray();
			ByteArrayInputStream bis = new ByteArrayInputStream(buf);
			return bis;
		} catch (Exception e) {
			return null;
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
		File f = new File(path);

		OutputStream out = null;
		try {
			if(!f.exists()){
				f.createNewFile();
			}
			out = new FileOutputStream(f);
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("1");
			HSSFRow[] row = new HSSFRow[datalist.size() + 1];// 行
			row[0] = sheet.createRow(0);
			// 创建列头
			HSSFCell[] headerCells = new HSSFCell[title.length];
			int[] colMaxWidths=new int[title.length];
			for (int i = 0; i < title.length; i++) {
				headerCells[i] = row[0].createCell(i);
				headerCells[i].setCellValue(title[i]);
				HSSFCellStyle cellStyle= wb.createCellStyle();
				HSSFFont font = wb.createFont();
				font.setBold(true);
				cellStyle.setFont(font);
				headerCells[i].setCellStyle(cellStyle);
				int columnWidth = title[i].toString().getBytes("GBK").length+1;
				colMaxWidths[i]=columnWidth;
			}
			// 往列中写数据
			Iterator iterator = datalist.iterator();
			int n_row = 1;
			while (iterator.hasNext()) {
				Object elmeRow=iterator.next();

				if(elmeRow instanceof Object[]) {
					Object[] datas = (Object[]) elmeRow;
					HSSFCell[] columCells = new HSSFCell[datas.length];// 列
					row[n_row] = sheet.createRow(n_row);
					for (int n_cel = 0; n_cel < datas.length; n_cel++) {
						columCells[n_cel] = row[n_row].createCell(n_cel);
						Object celValue=datas[n_cel];

						if(celValue==null){
							columCells[n_cel].setBlank();
						}else {
							int width=celValue.toString().getBytes("GBK").length+1;
							if(n_cel < colMaxWidths.length && colMaxWidths[n_cel]<width){
								colMaxWidths[n_cel]=width;
							}
							if (celValue instanceof String) {
								columCells[n_cel].setCellValue(celValue + "");
							}else if(celValue instanceof Double){
								columCells[n_cel].setCellValue((Double)celValue);
							}else if(celValue instanceof Boolean){
								columCells[n_cel].setCellValue((Boolean)celValue);
							}else if(celValue instanceof Date){
								columCells[n_cel].setCellValue((Date)celValue);
							}else if(celValue instanceof LocalDate){
								columCells[n_cel].setCellValue((LocalDate)celValue);
							}else if(celValue instanceof LocalDateTime){
								columCells[n_cel].setCellValue((LocalDateTime)celValue);
							}else{
								columCells[n_cel].setCellValue(celValue.toString());
							}
						}


					}
				}else if(elmeRow instanceof List){
					List list = (List) elmeRow;
					row[n_row] = sheet.createRow(n_row);
					HSSFCell[] columCells = new HSSFCell[list.size()];// 列
					for (int n_cel = 0; n_cel < list.size(); n_cel++) {
						columCells[n_cel] = row[n_row].createCell(n_cel);
						Object celValue=list.get(n_cel);
						if(celValue==null){
							columCells[n_cel].setBlank();
						}else {
							int width=celValue.toString().getBytes("GBK").length+1;
							if(n_cel < colMaxWidths.length && colMaxWidths[n_cel]<width){
								colMaxWidths[n_cel]=width;
							}
							if (celValue instanceof String) {
								columCells[n_cel].setCellValue(celValue + "");
							}else if(celValue instanceof Double){
								columCells[n_cel].setCellValue((Double)celValue);
							}else if(celValue instanceof Boolean){
								columCells[n_cel].setCellValue((Boolean)celValue);
							}else if(celValue instanceof Date){
								columCells[n_cel].setCellValue((Date)celValue);
							}else if(celValue instanceof LocalDate){
								columCells[n_cel].setCellValue((LocalDate)celValue);
							}else if(celValue instanceof LocalDateTime){
								columCells[n_cel].setCellValue((LocalDateTime)celValue);
							}else{
								columCells[n_cel].setCellValue(celValue.toString());
							}
						}
					}
				}
				n_row++;
			}
			sheet.setPrintGridlines(false);

			for (int i = 0; i < title.length; i++) {
				sheet.setColumnWidth(i, colMaxWidths[i]*300);
			}
			wb.write(out);
			out.flush();
		} catch (Exception e) {
			logger.error(""+e,e);
			return false;
		}finally {
			if(out!=null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

		return true;
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
		List<List<Object>> list = new ArrayList();
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(path));
			if (numSheets < 0 || numSheets >= workbook.getNumberOfSheets() || workbook.getSheetAt(numSheets) == null)
				return null;
			// 表单
			HSSFSheet aSheet = workbook.getSheetAt(numSheets);// 获得一个sheet
			// 行
			for (int irow = aSheet.getFirstRowNum(); irow <= aSheet.getLastRowNum(); irow++) {
				if (null == aSheet.getRow(irow))
					continue;
				HSSFRow aRow = aSheet.getRow(irow);
				List<Object> al = new ArrayList();
				// 列
				int maxColumNum=Math.min(aRow.getLastCellNum(),maxColum);
				for (int icel = 0; icel < maxColumNum; icel++) {
					Object cellValue = null;
					if (null != aRow.getCell(icel)) {
						HSSFCell aCell = aRow.getCell(icel);
						try {
							if (aCell.getCellType()== CellType.NUMERIC) {

								cellValue=aCell.getNumericCellValue();
								//获取日期类型
								short format = aCell.getCellStyle().getDataFormat();
								//此处运用了重写的isCellDateFormatted方法，在DateFormatUtil工具类中
								if (DateUtil.isCellDateFormatted(aCell)){
									SimpleDateFormat sdf =null;
									if (format == 20 || format == 32){
										sdf = new SimpleDateFormat("HH:mm");
									}else if (format == 14 || format == 31 || format == 57 || format == 58){
										sdf = new SimpleDateFormat("yyyy-MM-dd");
									}else {
										/**
										 * 上面处理的时间，单元格格式都是自定义格式中时间格式，而下面解决的是单元格格式为日期格式的问题
										 * 单元格格式为日期格式时，没办法通过format进行判断，所以进行如下操作：
										 * 1、现将数据转换成 "yyyy-MM-dd HH:mm:ss"的格式
										 * 2、将SimpleDateFormat类型的时间转换成String类型，并去除其中的空格、：和-三个符号，存进String类型数组中
										 * 3、通过数组进行中时分秒的数据进行判断最终要展示的时间样式
										 * 判断小时为0时，则不显示时分秒；判断秒钟为0时，则不显示年月日
										 */
										sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										String[] str = sdf.format(DateUtil.getJavaDate(aCell.getNumericCellValue())).toString().split("-|:| ");
										if (Integer.parseInt(str[3]) == 0 && Integer.parseInt(str[4]) == 0 && Integer.parseInt(str[5]) == 0){
											//根据表格，当时分秒都为0时，说明此单元格为生产日期，此处只显示年月日
											sdf = new SimpleDateFormat("yyyy-MM-dd");
										}else if (Integer.parseInt(str[3]) != 0 && Integer.parseInt(str[4]) != 0 && Integer.parseInt(str[5]) == 0){
											//根据表格，当小时和分钟不为0的情况，说明此单元格为初凝和终凝，年月日是不需要的
											sdf = new SimpleDateFormat("HH:mm");
										}else{
											break;
										}
									}
									Date date = DateUtil.getJavaDate(aCell.getNumericCellValue());
									cellValue = sdf.format(date);
								}

							} else if(aCell.getCellType()== CellType.FORMULA){
								switch (aCell.getCachedFormulaResultType()){
									case NUMERIC:
										cellValue=aCell.getNumericCellValue();
										break;
									case BOOLEAN:
										cellValue=aCell.getBooleanCellValue();
									default:
										cellValue = aCell.toString();
								}
							} else if(aCell.getCellType()==CellType.BLANK){
								cellValue=null;
							} else {
								cellValue =  aCell.toString();
							}
						} catch (Exception e) {
							throw e;
						}
					}else{
						cellValue=null;
					}
					al.add(cellValue);
				}

				list.add(al);// 添加一行数据
			}
		} catch (Exception e) {
			logger.error(""+e,e);
		}
		return list;
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
		List<Map<String,Object>> list = new ArrayList();
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(path));
			// 如果没有该Sheet，则返回NULL
			if (numSheets < 0 || numSheets >= workbook.getNumberOfSheets() || workbook.getSheetAt(numSheets) == null)
				return null;

			// 获得该Sheet
			HSSFSheet aSheet = workbook.getSheetAt(numSheets);

			// 读取标题行
			HSSFRow firstRow = aSheet.getRow(0);
			String title[] = new String[firstRow.getLastCellNum()];

			for (int icel = 0; icel < firstRow.getLastCellNum(); icel++) {
				title[icel] = firstRow.getCell(icel).getStringCellValue();

			}

			// 读取数据行
			for (int irow = 1; irow <= aSheet.getLastRowNum(); irow++) {
				if (null == aSheet.getRow(irow))
					continue;
				HSSFRow aRow = aSheet.getRow(irow);
				Map<String,Object> al = new HashMap<>();
				// 列
				int maxColumNum=Math.min(aRow.getLastCellNum(),title.length);
				for (int icel = 0; icel < maxColumNum; icel++) {
					Object cellValue = null;
					if (null != aRow.getCell(icel)) {
						HSSFCell aCell = aRow.getCell(icel);
						try {
							if (aCell.getCellType()== CellType.NUMERIC) {

								cellValue=aCell.getNumericCellValue();
								//获取日期类型
								short format = aCell.getCellStyle().getDataFormat();
								//此处运用了重写的isCellDateFormatted方法，在DateFormatUtil工具类中
								if (DateUtil.isCellDateFormatted(aCell)){
									SimpleDateFormat sdf =null;
									if (format == 20 || format == 32){
										sdf = new SimpleDateFormat("HH:mm");
									}else if (format == 14 || format == 31 || format == 57 || format == 58){
										sdf = new SimpleDateFormat("yyyy-MM-dd");
									}else {
										/**
										 * 上面处理的时间，单元格格式都是自定义格式中时间格式，而下面解决的是单元格格式为日期格式的问题
										 * 单元格格式为日期格式时，没办法通过format进行判断，所以进行如下操作：
										 * 1、现将数据转换成 "yyyy-MM-dd HH:mm:ss"的格式
										 * 2、将SimpleDateFormat类型的时间转换成String类型，并去除其中的空格、：和-三个符号，存进String类型数组中
										 * 3、通过数组进行中时分秒的数据进行判断最终要展示的时间样式
										 * 判断小时为0时，则不显示时分秒；判断秒钟为0时，则不显示年月日
										 */
										sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										String[] str = sdf.format(DateUtil.getJavaDate(aCell.getNumericCellValue())).toString().split("-|:| ");
										if (Integer.parseInt(str[3]) == 0 && Integer.parseInt(str[4]) == 0 && Integer.parseInt(str[5]) == 0){
											//根据表格，当时分秒都为0时，说明此单元格为生产日期，此处只显示年月日
											sdf = new SimpleDateFormat("yyyy-MM-dd");
										}else if (Integer.parseInt(str[3]) != 0 && Integer.parseInt(str[4]) != 0 && Integer.parseInt(str[5]) == 0){
											//根据表格，当小时和分钟不为0的情况，说明此单元格为初凝和终凝，年月日是不需要的
											sdf = new SimpleDateFormat("HH:mm");
										}else{
											break;
										}
									}
									Date date = DateUtil.getJavaDate(aCell.getNumericCellValue());
									cellValue = sdf.format(date);
								}

							} else if(aCell.getCellType()== CellType.FORMULA){
								switch (aCell.getCachedFormulaResultType()){
									case NUMERIC:
										cellValue=aCell.getNumericCellValue();
										break;
									case BOOLEAN:
										cellValue=aCell.getBooleanCellValue();
									default:
										cellValue = aCell.toString();
								}
							} else if(aCell.getCellType()==CellType.BLANK){
								cellValue=null;
							} else {
								cellValue =  aCell.toString();
							}
						} catch (Exception e) {
							throw e;
						}
					}else{
						cellValue=null;
					}
					if(icel<title.length) {
						al.put(title[icel], cellValue);
					}
				}

				list.add(al);// 添加一行数据
			}
		} catch (Exception e) {
			logger.error(""+e,e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] arg) {
		// 写入示例
		String title[] = { "厂商", "维保合同名称", "设备类型", "维保项目一", "维保项目二" };
		List datalist = new ArrayList();
		for (int i = 0; i < 6; i++) {
			String data[] = { "设备厂商" + i, "合同一" + i, "设备一" + i, "项目一" + i, "项目二" + i };
			datalist.add(data);
		}
		new ExcelUtilLt2007().writeExcel("c:\\1.xls", title, datalist);

	}
}
