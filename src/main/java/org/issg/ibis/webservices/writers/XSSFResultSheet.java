package org.issg.ibis.webservices.writers;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class XSSFResultSheet implements ResultSheet {
	
	private XSSFSheet sheet;
	private int rowNum = 0;

	public XSSFResultSheet(XSSFSheet sheet) {
		this.sheet = sheet;
	}

	@Override
	public void createRow(List<String> cellValues) {
		XSSFRow row = sheet.createRow(rowNum);
		rowNum++;

		int col = 0;
		for (String string : cellValues) {
			XSSFCell cell = row.createCell(col);
			cell.setCellValue(string);
			col++;
		}
	}

	@Override
	public void close() {
		//No-op
	}

}
