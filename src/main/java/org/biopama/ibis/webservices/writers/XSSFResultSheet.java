package org.biopama.ibis.webservices.writers;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class XSSFResultSheet implements WritableSheet {
	
	private Sheet sheet;
	private int rowNum = 0;

	public XSSFResultSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	@Override
	public void createRow(List<String> cellValues) {
		Row row = sheet.createRow(rowNum);
		rowNum++;

		int col = 0;
		for (String string : cellValues) {
			Cell cell = row.createCell(col);
			cell.setCellValue(string);
			col++;
		}
	}

	@Override
	public void close() {
		//No-op
	}

	@Override
	public void setFirstRow(int firstRow) {
		rowNum = firstRow;
	}

}
