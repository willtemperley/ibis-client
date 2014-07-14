package org.issg.ibis.webservices.writers;

import java.util.List;

public interface ResultSheet {

	void createRow(List<String> cells);

	void close();
}
