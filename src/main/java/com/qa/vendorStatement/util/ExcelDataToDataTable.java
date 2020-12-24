package com.qa.vendorStatement.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import cucumber.api.DataTable;
import cucumber.api.Transformer;
import cucumber.runtime.ParameterInfo;
import cucumber.runtime.xstream.LocalizedXStreams;
import gherkin.formatter.model.DataTableRow;

public class ExcelDataToDataTable extends Transformer<DataTable>{

	@Override
	public DataTable transform(String filePath) {
		
		StringTokenizer st = new StringTokenizer(filePath, ",");
		String path = st.nextToken();
		String sheetName = st.nextToken();
		
		ExcelReader reader = new ExcelReader.ExcelReaderBuilder().setFileLocation(path).setSheet(sheetName).build();
		
		List<List<String>> excelData = getExcelData(reader);
		
		List<DataTableRow> dataTableRows = getDataTableRows(excelData);
		
		DataTable table = getDataTable(dataTableRows);
		
		return table;
	}

	private DataTable getDataTable(List<DataTableRow> dataTableRows) {
		ParameterInfo parameterInfo = new ParameterInfo(null, null, null, null);
		cucumber.runtime.table.TableConverter tableConverter = new cucumber.runtime.table.TableConverter(new LocalizedXStreams(Thread.currentThread().getContextClassLoader()).get(Locale.getDefault()), parameterInfo);
		DataTable table = new DataTable(dataTableRows, (cucumber.runtime.table.TableConverter) tableConverter);
		return table;
	}

	private List<DataTableRow> getDataTableRows(List<List<String>> excelData) {
		List<DataTableRow> dataTableRows = new LinkedList<>();
		int line=1;
		for(List<String> list: excelData) {
			gherkin.formatter.model.Comment comment = new gherkin.formatter.model.Comment("", line);
			DataTableRow tableRow = new DataTableRow(Arrays.asList(comment), list, line++);
			dataTableRows.add(tableRow);
		}
		return dataTableRows;
	}

	private List<List<String>> getExcelData(ExcelReader reader) {
		List<List<String>> excelData = new LinkedList<>();
		try {
			excelData = reader.getSheetData();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return excelData;
	}

}
