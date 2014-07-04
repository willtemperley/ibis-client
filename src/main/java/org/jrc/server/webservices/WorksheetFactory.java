package org.jrc.server.webservices;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.WrapDynaBean;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.mysema.query.types.Path;

public class WorksheetFactory {

	private List<Path<?>> cols = new ArrayList<Path<?>>();

	private int currentRow = 0;

	public <X> void addColumn(Path<X> id) {
		cols.add(id);
		id.getRoot().getMetadata().getName();
	}
	
	public void writeSheet(XSSFSheet worksheet, List<?> rows) {
		Row r = worksheet.createRow(currentRow);
		for (Path<?> p : cols) {
			addCell(r, p.getMetadata().getName());
		}
		this.currentRow = 1;
		for (Object object : rows) {
			r = worksheet.createRow(currentRow);
			addCells(r, object);
			currentRow++;
		}
	}

	/**
	 * Append all the values in the bean to a row
	 * 
	 * @param row
	 * @param value
	 */
	private void addCells(Row row, Object value) {
		short num = row.getLastCellNum();
		if (num < 0) {
			num = 0;
		}
	
		WrapDynaBean db = new WrapDynaBean(value);
	
		for (Path<?> p : cols) {
			Cell c = row.createCell(num, Cell.CELL_TYPE_STRING);
	
			Object v = db.get(p.getMetadata().getName());
	
			if (v != null) {
				c.setCellValue(v.toString());
			}
			num++;
		}
	}

	/**
	 * Appends a value to the end of a row
	 * 
	 * @param row
	 * @param value
	 */
	protected void addCell(Row row, Object value) {
		short num = row.getLastCellNum();
		if (num < 0) {
			num = 0;
		}
		Cell c = row.createCell(num, Cell.CELL_TYPE_STRING);
		if (value != null) {
			c.setCellValue(value.toString());
		}
	}

}