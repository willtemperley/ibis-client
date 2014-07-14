package org.issg.ibis.webservices.writers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Joiner;

public class CsvResultSheet implements ResultSheet{

	private FileWriter fw;

	public CsvResultSheet(File temp) {
		try {
			fw = new FileWriter(temp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createRow(List<String> cells) {
		try {
			fw.write(Joiner.on(',').join(cells));
			fw.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		try {
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
