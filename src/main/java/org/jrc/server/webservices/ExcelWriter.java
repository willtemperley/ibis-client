package org.jrc.server.webservices;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.issg.ibis.domain.SpeciesImpact;

public class ExcelWriter implements SpeciesOutputWriter {

	private XSSFWorkbook workbook;
	private int currentRow = 0;
	private XSSFSheet s;

	public ExcelWriter(List<String> headers) {

		workbook = new XSSFWorkbook();
		workbook.createSheet("Sheet1");
		s = workbook.getSheetAt(0);
		
		
		Row r = s.createRow(currentRow);
		for (String string : headers) {
			addCell(r, string);
		}
		this.currentRow = 1;

	}

	@Override
	public void writeRow(SpeciesImpact si) {

		Row r = s.createRow(currentRow);
		addCell(r, si.getThreatenedSpecies());
		addCell(r, si.getInvasiveSpecies());
		addCell(r, si.getImpactMechanism());
		addCell(r, si.getImpactOutcome());
		addCell(r, si.getBiologicalStatus());
		addCell(r, si.getLocation());
		addCell(r, si.getLocation().getCountry());
		currentRow++;
	}

	private void addCell(Row r, Object value) {
		short num = r.getLastCellNum();
		if(num < 0) {
			num = 0;
		}
		Cell c = r.createCell(num, Cell.CELL_TYPE_STRING);
		if (value != null) {
			c.setCellValue(value.toString());
		}
	};

	public void write(ServletOutputStream outputStream) throws IOException {
		// TODO Auto-generated method stub
		workbook.write(outputStream);
	}

}