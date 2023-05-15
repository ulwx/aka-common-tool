package com.ulwx.tool.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteExcel {

	private HSSFWorkbook wb = new HSSFWorkbook();

	private HSSFSheet sheet = wb.createSheet();
	
	private HSSFCellStyle cellstyle = null;


	public WriteExcel() {

	}

	/**
	 * 定义列宽
	 * 
	 * @param columnIndex
	 * @param width
	 */
	public void setColumnWidth(int columnIndex, int width) {
		sheet.setColumnWidth(columnIndex, width);
	}

	public HSSFCellStyle createDefaultCellStyle() {
		// 创建单元格样式
		HSSFCellStyle cellStyle = wb.createCellStyle();

		// 指定单元格居中对齐
		cellStyle.setAlignment(HorizontalAlignment.CENTER);

		// 指定单元格垂直居中对齐
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// 指定当单元格内容显示不下时自动换行
		cellStyle.setWrapText(true);

		// 设置单元格字体
		HSSFFont font = wb.createFont();
		//font.setBold(true);
		font.setFontName("宋体");
		font.setFontHeight((short) 200);
		cellStyle.setFont(font);
		return cellStyle;

	}

	public HSSFCellStyle createCellStyle() {
		// 创建单元格样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		return cellStyle;

	}
	
	public HSSFCellStyle createCellStyle(short alignment,
			 VerticalAlignment verticalAlignment, boolean isWrap, short fontHeight) {
		// 创建单元格样式
		HSSFCellStyle cellStyle = wb.createCellStyle();

		// 指定单元格居中对齐
		cellStyle.setAlignment(HorizontalAlignment.CENTER);

		// 指定单元格垂直居中对齐
		cellStyle.setVerticalAlignment(verticalAlignment);

		// 指定当单元格内容显示不下时自动换行
		cellStyle.setWrapText(isWrap);

		// 设置单元格字体
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeight((short) fontHeight);
		cellStyle.setFont(font);
		return cellStyle;

	}

	/**
	 * @return the sheet
	 */
	public HSSFSheet getSheet() {
		return sheet;
	}

	/**
	 * @param sheet
	 *            the sheet to set
	 */
	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

	/**
	 * @return the wb
	 */
	public HSSFWorkbook getWb() {
		return wb;
	}

	/**
	 * @param wb
	 *            the wb to set
	 */
	public void setWb(HSSFWorkbook wb) {
		this.wb = wb;
	}

	/**
	 * 创建通用EXCEL头部
	 * 
	 * @param headString
	 *            头部显示的字符
	 * @param colSum
	 *            该报表的列数
	 */
	public void createNormalFirstHead(String headString, int colSum) {

		HSSFRow row = sheet.createRow(0);

		// 设置第一行
		HSSFCell cell = row.createCell(0);
		row.setHeight((short) 400);

		// 定义单元格为字符串类型
		cell.setCellType(CellType.STRING);
		cell.setCellValue(new HSSFRichTextString(headString));

		// 指定合并区域
		this.addMergedRegion(0, 0, 0, colSum);

		HSSFCellStyle cellStyle = wb.createCellStyle();

		cellStyle.setAlignment(HorizontalAlignment.CENTER); // 指定单元格居中对齐
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 指定单元格垂直居中对齐
		cellStyle.setWrapText(true);// 指定单元格自动换行

		// 设置单元格字体
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeight((short) 300);
		cellStyle.setFont(font);

		cell.setCellStyle(cellStyle);
	}

	/**
	 * 创建通用报表第二行
	 * 
	 * @param params
	 *            统计条件数组
	 * @param colSum
	 *            需要合并到的列索引
	 */
	public void createNormalSecondRow(String[] params, int colSum) {
		HSSFRow row1 = sheet.createRow(1);
		row1.setHeight((short) 300);

		HSSFCell cell2 = row1.createCell(0);

		cell2.setCellType(CellType.STRING);
		cell2.setCellValue(new HSSFRichTextString("统计时间：" + params[0] + "至"
				+ params[1]));

		// 指定合并区域
		this.addMergedRegion(1, 0, 1, colSum);

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER); // 指定单元格居中对齐
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 指定单元格垂直居中对齐
		cellStyle.setWrapText(true);// 指定单元格自动换行

		// 设置单元格字体
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeight((short) 250);
		cellStyle.setFont(font);

		cell2.setCellStyle(cellStyle);

	}

	/**
	 * 设置报表标题
	 * 
	 * @param columHeader
	 *            标题字符串数组
	 */
	public void createColumHeader(String[] columHeader) {

		// 设置列头
		HSSFRow row2 = sheet.createRow(2);

		// 指定行高
		row2.setHeight((short) 600);

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER); // 指定单元格居中对齐
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 指定单元格垂直居中对齐
		cellStyle.setWrapText(true);// 指定单元格自动换行

		// 单元格字体
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeight((short) 250);
		cellStyle.setFont(font);

		/*
		 * cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单无格的边框为粗体
		 * cellStyle.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
		 * cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		 * cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		 * cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		 * cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
		 * cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		 * cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
		 */

		// 设置单元格背景色
		//cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		//cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		HSSFCell cell3 = null;

		for (int i = 0; i < columHeader.length; i++) {
			cell3 = row2.createCell(i);
			cell3.setCellType(CellType.STRING);
			cell3.setCellStyle(cellStyle);
			cell3.setCellValue(new HSSFRichTextString(columHeader[i]));
		}

	}

	/**
	 * 
	 * @param rowIndex
	 *            ，建立第几行，0表示第1行，1表示第二行
	 */
	public HSSFRow createRow(int rowIndex) {
		HSSFRow row = sheet.createRow(rowIndex);
		return row;
	}

	public void addMergedRegion(int rowFrom, int colFrom, int rowTo, int colTo) {
		CellRangeAddress cr = new CellRangeAddress(rowFrom, rowTo, colFrom,
				colTo);
		sheet.addMergedRegion(cr);
	}

	/**
	 * 创建内容单元格
	 * 
	 * @param row
	 *            HSSFRow
	 * @param col
	 *            short型的列索引
	 * @param align
	 *            对齐方式
	 * @param val
	 *            列值
	 */
	public void cteateCell(HSSFRow row, int col, HorizontalAlignment align, String val,
						   CellType cellType,HSSFCellStyle cellstyle) {
		HSSFCell cell = row.createCell(col);
		cell.setCellType(cellType);
		if (cellType == CellType.NUMERIC) {
			cell.setCellValue(Double.valueOf(val));
		} else {
			cell.setCellValue(new HSSFRichTextString(val));
		}

		//HSSFCellStyle cellstyle = wb.createCellStyle();
		cellstyle.setAlignment(align);
		cell.setCellStyle(cellstyle);
	}

	/**
	 * 创建money格式的单元格
	 * 
	 * @param row
	 * @param col
	 * @param align
	 * @param val
	 */
	public void cteateMoneyCell(HSSFRow row, int col, HorizontalAlignment align, String val,HSSFCellStyle cellstyle) {
		HSSFCell cell = row.createCell(col);
		cell.setCellValue(Double.valueOf(val));
		//cellstyle = wb.createCellStyle();
		HSSFDataFormat format = wb.createDataFormat();
		cellstyle.setDataFormat(format.getFormat("¥#,#0.00"));
		cellstyle.setAlignment(align);
		cell.setCellStyle(cellstyle);
	}

	/**
	 * 创建百分比格式的单元格
	 * 
	 * @param row
	 * @param col
	 * @param align
	 * @param val
	 */
	public void cteatePercentCell(HSSFRow row, int col, HorizontalAlignment align, String val,HSSFCellStyle cellstyle) {
		HSSFCell cell = row.createCell(col);
		cell.setCellValue(Double.valueOf(val) / 100);
		//cellstyle = wb.createCellStyle();
		HSSFDataFormat format = wb.createDataFormat();
		cellstyle.setDataFormat(format.getBuiltinFormat("0.00%"));
		cellstyle.setAlignment(align);
		cell.setCellStyle(cellstyle);
	}

	public void cteateCell(HSSFRow row, int col, HorizontalAlignment align, String val) {
		HSSFCell cell = row.createCell(col);
		cell.setCellType(CellType.STRING);
		cell.setCellValue(new HSSFRichTextString(val));
		cellstyle = wb.createCellStyle();
		cellstyle.setAlignment(align);
		cell.setCellStyle(cellstyle);
	}

	public void cteateCell(HSSFRow row, int col, short align, String val,
			HSSFCellStyle cellstyle) {
		HSSFCell cell = row.createCell(col);
		cell.setCellType(CellType.STRING);
		cell.setCellValue(new HSSFRichTextString(val));

		cell.setCellStyle(cellstyle);
	}

	/**
	 * 创建合计行
	 * 
	 * @param colSum
	 *            需要合并到的列索引
	 * @param cellValue
	 */
	public void createLastSumRow(int colSum, String[] cellValue) {

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER); // 指定单元格居中对齐
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 指定单元格垂直居中对齐
		cellStyle.setWrapText(true);// 指定单元格自动换行

		// 单元格字体
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeight((short) 250);
		cellStyle.setFont(font);

		HSSFRow lastRow = sheet.createRow((short) (sheet.getLastRowNum() + 1));
		HSSFCell sumCell = lastRow.createCell(0);

		sumCell.setCellValue(new HSSFRichTextString("合计"));
		sumCell.setCellStyle(cellStyle);
		this.addMergedRegion(sheet.getLastRowNum(), 0, sheet.getLastRowNum(),
				colSum);// 指定合并区域

		for (int i = 2; i < (cellValue.length + 2); i++) {
			sumCell = lastRow.createCell(i);
			sumCell.setCellStyle(cellStyle);
			sumCell.setCellValue(new HSSFRichTextString(cellValue[i - 2]));

		}

	}

	/**
	 * 输入EXCEL文件
	 * 
	 * @param fileName
	 *            文件名
	 */
	public void outputExcel(String fileName) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(fileName));
			wb.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
