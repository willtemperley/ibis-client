package org.issg.excel.download;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.vaadin.server.StreamResource.StreamSource;

public class ExcelStreamSource implements StreamSource {
	
	private XSSFWorkbook workbook;
	private int currentRow;
	private XSSFSheet s;

	public ExcelStreamSource() {

		workbook = new XSSFWorkbook();
		workbook.createSheet("Sheet1");
		this.currentRow = 0;
		s = workbook.getSheetAt(0);

	}
	
	
	@Override
	public InputStream getStream() {

		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			return new ByteArrayInputStream(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public Row addRow(List<String> items) {
		Row r = s.createRow(currentRow);
		int i = 0;
		for (String item: items) {
			Cell c = r.createCell(i++, Cell.CELL_TYPE_STRING);
			c.setCellValue(item);
		}
		currentRow++;
		return r;
	}
}