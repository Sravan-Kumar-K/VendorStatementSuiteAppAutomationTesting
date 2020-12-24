package com.qa.vendorStatement.util;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	private String fileName;
	private String sheetName;
	private int sheetIndex;
	private XSSFWorkbook book;

	private ExcelReader(ExcelReaderBuilder excelReaderBuilder) {
		this.fileName = excelReaderBuilder.fileName;
		this.sheetIndex = excelReaderBuilder.sheetIndex;
		this.sheetName = excelReaderBuilder.sheetName;
	}

	public static class ExcelReaderBuilder {

		private String fileName;
		private String sheetName;
		private int sheetIndex;

		public ExcelReaderBuilder setFileLocation(String location) {
			this.fileName = location;
			return this;
		}

		public ExcelReaderBuilder setSheet(String sheetName) {
			this.sheetName = sheetName;
			return this;
		}

		public ExcelReaderBuilder setSheet(int index) {
			this.sheetIndex = index;
			return this;
		}

		public ExcelReader build() {
			return new ExcelReader(this);
		}

	}

	private XSSFWorkbook getWorkBook(String filePath) throws InvalidFormatException, IOException {
		return new XSSFWorkbook(new File(filePath));
	}

	private XSSFSheet getWorkBookSheet(String fileName, String sheetName) throws InvalidFormatException, IOException {
		this.book = getWorkBook(fileName);
		return this.book.getSheet(sheetName);
	}

	private XSSFSheet getWorkBookSheet(String fileName, int sheetIndex) throws InvalidFormatException, IOException {
		this.book = getWorkBook(fileName);
		return this.book.getSheetAt(sheetIndex);
	}

	public List<List<String>> getSheetData() throws IOException{
		XSSFSheet sheet;
		List<List<String>> outerList = new LinkedList<>();
		
		try {
			sheet = getWorkBookSheet(fileName, sheetName);
			outerList = getSheetData(sheet);
		} catch (InvalidFormatException e) {
			throw new RuntimeException(e.getMessage());
		}finally {
			//this.book.close();
		}
		return outerList;
	}
	
	public List<List<String>> getSheetDataAt() throws InvalidFormatException, IOException {
		
		XSSFSheet sheet;
		List<List<String>> outerList = new LinkedList<>();
		
		try {
			sheet = getWorkBookSheet(fileName, sheetIndex);
			outerList = getSheetData(sheet);
		} catch (InvalidFormatException e) {
			throw new RuntimeException(e.getMessage());
		}finally {
			this.book.close();
		}
		return outerList;
	}

	private List<List<String>> getSheetData(XSSFSheet sheet) {
		List<List<String>> outerList = new LinkedList<>();
		prepareOutterList(sheet, outerList);
		return Collections.unmodifiableList(outerList);
	}

	private void prepareOutterList(XSSFSheet sheet, List<List<String>> outerList) {
		int columnCount = sheet.getRow(0).getLastCellNum();
		for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
			List<String> innerList = new LinkedList<>();
			XSSFRow xssfRow = sheet.getRow(i);
			if(xssfRow!=null) {
				for (int j =0; j < xssfRow.getLastCellNum(); j++) {
					prepareInnerList(innerList, xssfRow, j);
				}
				outerList.add(Collections.unmodifiableList(innerList));
			}else {
				List<String> emptyList = new LinkedList<>();
				for(int i1=0;i1<columnCount;i1++)
					emptyList.add("");
				outerList.add(emptyList);
			}
		}
	}

	private void prepareInnerList(List<String> innerList, XSSFRow xssfRow, int j) {
//		CellType cellType = xssfRow.getCell(j).getCellTypeEnum();
//		System.out.println(cellType);
		XSSFCell cell = xssfRow.getCell(j);
		if(cell!=null) {
			switch (xssfRow.getCell(j).getCellTypeEnum()) {
			case BLANK:
				innerList.add("");
				break;
	
			case STRING:
				innerList.add(xssfRow.getCell(j).getStringCellValue());
				break;
	
			case NUMERIC:
				innerList.add(xssfRow.getCell(j).getNumericCellValue() + "");
				break;
	
			case BOOLEAN:
				innerList.add(xssfRow.getCell(j).getBooleanCellValue() + "");
				break;
			
			case _NONE:
				innerList.add("");
				break;
				
			default:
				throw new IllegalArgumentException("Cannot read the column : " + j);
			}
		}else {
			innerList.add("");
		}
			
	}
}
