package org.issg.ibis.webservices.writers;

import javax.servlet.ServletOutputStream;


public interface ResultWriter {

	ResultSheet createSheet(String string);

	void write(ServletOutputStream out);

}
