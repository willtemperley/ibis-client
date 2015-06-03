package org.biopama.ibis.webservices.writers;

import java.io.IOException;

import javax.servlet.ServletOutputStream;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XSSFResultWriter implements ResultWriter {
	
	private XSSFWorkbook workbook;

	public XSSFResultWriter() {
		workbook = new XSSFWorkbook();
	}

	@Override
	public WritableSheet createSheet(String string) {
		XSSFSheet s = workbook.createSheet(string);
		return new XSSFResultSheet(s);
	}

	@Override
	public void write(ServletOutputStream out) {
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
