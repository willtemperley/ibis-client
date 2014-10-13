package org.issg.ibis.webservices.writers;

import java.util.List;

public interface WritableSheet {
	
	void setFirstRow(int firstRow);

	void createRow(List<String> cells);

	void close();
}
