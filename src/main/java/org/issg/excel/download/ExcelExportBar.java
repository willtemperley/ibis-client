package org.issg.excel.download;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

public class ExcelExportBar extends HorizontalLayout {

	public ExcelExportBar() {
		Button b = new Button("Excel");
		StreamResource myResource = Exporter.createResource();
		FileDownloader fileDownloader = new FileDownloader(myResource);
		fileDownloader.extend(b);
		addComponent(b);
	}

}
