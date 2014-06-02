package org.issg.excel.download;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;

public class Exporter {

	private static final class ExcelStreamSource implements StreamSource {
		@Override
		public InputStream getStream() {

			Workbook workbook = new XSSFWorkbook();

			workbook.createSheet("x");
			Sheet s = workbook.getSheetAt(0);
			Row r = s.createRow(2);
			Cell c = r.createCell(0, Cell.CELL_TYPE_STRING);
			c.setCellValue("X");

			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				return new ByteArrayInputStream(bos.toByteArray());
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

		}
	}

	public static StreamResource createResource() {

		return new StreamResource(new ExcelStreamSource(), "myexcel.xlsx");
	}

}
