package org.issg.ibis.webservices;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.WrapDynaBean;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.issg.ibis.webservices.writers.WritableSheet;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Path;

public class WorksheetWriter {

	private List<Path<?>> cols = new ArrayList<Path<?>>();

	private boolean writeHeader = true;
	
	public void setWriteHeader(boolean writeHeader) {
		this.writeHeader = writeHeader;
	}

	public <X> void addColumn(Path<X> id) {
		cols.add(id);
		id.getRoot().getMetadata().getName();
	}

	public void writeSheet(WritableSheet worksheet, List<?> rows) {
		if (writeHeader) {
			List<String> cells = new ArrayList<String>();
			for (Path<?> p : cols) {
				cells.add(p.getMetadata().getName());
			}
			worksheet.createRow(cells);
		}

		for (Object object : rows) {
			List<String> l = getCells(object);
			worksheet.createRow(l);
		}

		worksheet.close();
	}

	/**
	 * Append all the values in the bean to a row
	 * 
	 * @param row
	 * @param value
	 */
	private List<String> getCells(Object value) {

		List<String> cells = new ArrayList<String>();
		WrapDynaBean db = new WrapDynaBean(value);

		for (Path<?> p : cols) {
			// Cell c = row.createCell(num, Cell.CELL_TYPE_STRING);

			Object v = db.get(p.getMetadata().getName());

			if (v != null) {
				cells.add(v.toString());
			} else {
				cells.add("");
			}
		}
		return cells;
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