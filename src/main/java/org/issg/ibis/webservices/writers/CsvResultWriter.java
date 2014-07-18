package org.issg.ibis.webservices.writers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;

import com.google.common.io.Files;


public class CsvResultWriter implements ResultWriter {
	
	Map<String, File> files = new HashMap<String, File>();

	@Override
	public ResultSheet createSheet(String string) {
		try {
			File temp = File.createTempFile("temp", ".csv");
			files.put(string + ".csv", temp);
			return new CsvResultSheet(temp);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void write(ServletOutputStream out) {
		try {
			out.write(createZip(files));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static byte[] createZip(Map<String, File> files) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zipfile = new ZipOutputStream(bos);
		Iterator<String> i = files.keySet().iterator();
		String fileName = null;
		ZipEntry zipentry = null;
		while (i.hasNext()) {
			fileName = (String) i.next();
			zipentry = new ZipEntry(fileName);
			zipfile.putNextEntry(zipentry);
			File f = files.get(fileName);	
			byte[] binary = Files.toByteArray(f);
			zipfile.write(binary);
		}
		zipfile.close();
		return bos.toByteArray();
	}

}
