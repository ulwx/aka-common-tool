package com.ulwx.tool.excel;

import com.ulwx.tool.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author daiqiang
 * @email dq007dq@qq.com
 */
public class XlsUtil {

	private final static Logger logger = LoggerFactory.getLogger(XlsUtil.class);

	/**
	 * 读取xls
	 * 
	 * @param inputPath
	 * @return
	 */
	public static Map<String, String[][]> readXls(String inputPath) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(inputPath));
			return readXls(workbook);
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 读取xls
	 * 
	 * @param inputStream
	 * @return
	 */
	public static Map<String, String[][]> readXls(InputStream inputStream) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			return readXls(workbook);
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}

	private static Map<String, String[][]> readXls(HSSFWorkbook workbook) {
		try {
			Map<String, String[][]> result = new HashMap<String, String[][]>();
			int sheetsCount = workbook.getNumberOfSheets();
			for (int sheetNum = 0; sheetNum < sheetsCount; sheetNum++) {
				HSSFSheet sheet = workbook.getSheetAt(sheetNum);
				int lastRowCount = sheet.getLastRowNum();
				int maxCellCount = 0;
				List<List<String>> sheetData = new ArrayList<List<String>>();
				for (int rowNum = 0; rowNum < lastRowCount; rowNum++) {
					ArrayList<String> rowDatas = new ArrayList<String>();
					HSSFRow row = sheet.getRow(rowNum);
					short firstCellNum = row.getFirstCellNum();
					short lastCellNum = row.getLastCellNum();
					if (lastCellNum > maxCellCount) {
						maxCellCount = lastCellNum;
					}
					for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
						HSSFCell cell = row.getCell(cellNum);
						if (cell != null) {
							String cellData = cell2String(cell);
							rowDatas.add(cellData);
						} else {
							rowDatas.add("");
						}
					}
					sheetData.add(rowDatas);
				}
				String[][] tmp = new String[lastRowCount + 1][maxCellCount + 1];
				for (int i = 0; i < sheetData.size(); i++) {
					List<String> rowData = sheetData.get(i);
					for (int j = 0; j < rowData.size(); j++) {
						String cellData = rowData.get(j);
						tmp[i][j] = cellData;
					}
				}
				result.put(sheetNum + "", tmp);
			}
			return result;
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}

	private static HSSFWorkbook createWorkbook(Map<String, String[][]> data, String templatePath) throws FileNotFoundException, IOException {
		HSSFWorkbook workbook = null;
		if (!StringUtils.isEmpty(templatePath)) {
			workbook = new HSSFWorkbook(new FileInputStream(templatePath));
		} else {
			workbook = new HSSFWorkbook();
		}
		Set<String> keySet = data.keySet();
		for (String key : keySet) {
			String[][] sheetData = data.get(key);
			HSSFSheet sheet = workbook.createSheet();
			for (int i = 0; i < sheetData.length; i++) {
				HSSFRow row = sheet.createRow(i);
				for (int j = 0; j < sheetData[i].length; j++) {
					HSSFCell cell = row.createCell(j);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(sheetData[i][j]);
				}
			}
		}
		return workbook;
	}

	/**
	 * 写入xls
	 * 
	 * @param data
	 * @param outputPath
	 */
	public static void writeXls(Map<String, String[][]> data, String outputPath) {
		try {
			HSSFWorkbook workbook = createWorkbook(data, null);
			FileOutputStream fos = new FileOutputStream(outputPath);
			workbook.write(fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 写入xls
	 * 
	 * @param sheetName
	 * @param data
	 * @param outputPath
	 */
	public static void writeXls(String sheetName, String[][] data, String outputPath) {
		Map<String, String[][]> datas = new HashMap<String, String[][]>();
		datas.put(sheetName, data);
		writeXls(datas, outputPath);
	}

	/**
	 * 写入xls
	 * 
	 * @param data
	 * @param templatePath
	 * @param outputPath
	 */
	public static void writeXls(Map<String, String[][]> data, String templatePath, String outputPath) {
		try {
			HSSFWorkbook workbook = createWorkbook(data, templatePath);
			FileOutputStream fos = new FileOutputStream(outputPath);
			workbook.write(fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 写入xls
	 * 
	 * @param sheetName
	 * @param data
	 * @param templatePath
	 * @param outputPath
	 */
	public static void writeXls(String sheetName, String[][] data, String templatePath, String outputPath) {
		Map<String, String[][]> datas = new HashMap<String, String[][]>();
		datas.put(sheetName, data);
		writeXls(datas, templatePath, outputPath);
	}

	/**
	 * 获取xls的OutputStream
	 * 
	 * @param data
	 * @return
	 */
	public static OutputStream getXlsOutputStream(Map<String, String[][]> data) {
		try {
			HSSFWorkbook workbook = createWorkbook(data, null);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			bos.flush();
			bos.close();
			return bos;
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取xls的OutputStream
	 * 
	 * @param sheetName
	 * @param data
	 * @return
	 */
	public static OutputStream getXlsOutputStream(String sheetName, String[][] data) {
		Map<String, String[][]> datas = new HashMap<String, String[][]>();
		datas.put(sheetName, data);
		return getXlsOutputStream(datas);
	}

	/**
	 * 获取xls的OutputStream
	 * 
	 * @param data
	 * @param templatePath
	 * @return
	 */
	public static OutputStream getXlsOutputStream(Map<String, String[][]> data, String templatePath) {
		try {
			HSSFWorkbook workbook = createWorkbook(data, templatePath);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			bos.flush();
			bos.close();
			return bos;
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取xls的OutputStream
	 * 
	 * @param sheetName
	 * @param data
	 * @param templatePath
	 * @return
	 */
	public static OutputStream getXlsOutputStream(String sheetName, String[][] data, String templatePath) {
		Map<String, String[][]> datas = new HashMap<String, String[][]>();
		datas.put(sheetName, data);
		return getXlsOutputStream(datas, templatePath);
	}

	/**
	 * 获取xls的InputStream
	 * 
	 * @param data
	 * @return
	 */
	public static InputStream getXlsInputStream(Map<String, String[][]> data) {
		try {
			HSSFWorkbook workbook = createWorkbook(data, null);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			bos.flush();
			bos.close();
			return outputStream2InputStream(bos);
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取xls的InputStream
	 * 
	 * @param sheetName
	 * @param data
	 * @return
	 */
	public static InputStream getXlsInputStream(String sheetName, String[][] data) {
		Map<String, String[][]> datas = new HashMap<String, String[][]>();
		datas.put(sheetName, data);
		return getXlsInputStream(datas);
	}

	/**
	 * 获取xls的InputStream
	 * 
	 * @param data ：每个sheet对应的表，每个表用行和列表示，String[][]第一维为行，第二维为列，data的map的键为sheet的名称
	 * @param templatePath
	 * @return
	 */
	public static InputStream getXlsInputStream(Map<String, String[][]> data, String templatePath) {
		try {
			HSSFWorkbook workbook = createWorkbook(data, templatePath);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			bos.flush();
			bos.close();
			return outputStream2InputStream(bos);
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取xls的InputStream
	 * 
	 * @param sheetName
	 * @param data
	 * @param templatePath
	 * @return
	 */
	public static InputStream getXlsInputStream(String sheetName, String[][] data, String templatePath) {
		Map<String, String[][]> datas = new HashMap<String, String[][]>();
		datas.put(sheetName, data);
		return getXlsInputStream(datas, templatePath);
	}

	private static String cell2String(HSSFCell cell, String dataFormat) throws Exception {
		switch (cell.getCellType()) {
			case NUMERIC: {// '\0'
				if ( DateUtil.isCellDateFormatted(cell)) {
					if (dataFormat == null) {
						dataFormat = "yyyy-mm-dd HH:MM:ss";
					}
					DateFormat sdf = new SimpleDateFormat(dataFormat);
					return sdf.format(cell.getDateCellValue());
				} else {
					return String.valueOf(cell.getNumericCellValue());
				}
			}
			case STRING: {// '\001'
				return cell.getStringCellValue();
			}
			case FORMULA: {// '\002'
				return cell.getCellFormula();
			}
			case BLANK: { // '\003'
				return "";
			}
			case BOOLEAN: { // '\004'
				return cell.getBooleanCellValue() ? "true" : "false";
			}
			case ERROR: { // '\005'
				return "error";
			}
		}
		return (new StringBuilder()).append("Unknown Cell Type: ").append(cell.getCellType()).toString();
	}

	private static String cell2String(HSSFCell cell) throws Exception {
		return cell2String(cell, null);
	}

	private static InputStream outputStream2InputStream(OutputStream os) {
		if (os instanceof ByteArrayOutputStream) {
			try {
				ByteArrayOutputStream bos = (ByteArrayOutputStream)os;
				bos.flush();
				byte[] buf = bos.toByteArray();
				ByteArrayInputStream bis = new ByteArrayInputStream(buf);
				return bis;
			} catch (IOException e) {
				logger.error("", e);
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("传入的参数不是ByteArrayOutputStream!");
		}
	}

	public static void main(String[] args) throws Exception {
		String inputPath = "C:\\export.xls";
		Map<String, String[][]> x = XlsUtil.readXls(inputPath);
		System.out.println(x);
		OutputStream os = XlsUtil.getXlsOutputStream(x, null);
		ByteArrayOutputStream bos = (ByteArrayOutputStream)os;
		FileOutputStream fos = new FileOutputStream("c:\\x.xls");
		bos.flush();
		bos.writeTo(fos);
		bos.close();
		fos.close();
	}

}
