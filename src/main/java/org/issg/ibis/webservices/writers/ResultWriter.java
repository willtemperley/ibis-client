package org.issg.ibis.webservices.writers;

import javax.servlet.ServletOutputStream;


public interface ResultWriter {

	WritableSheet createSheet(String string);

	void write(ServletOutputStream out);

}
