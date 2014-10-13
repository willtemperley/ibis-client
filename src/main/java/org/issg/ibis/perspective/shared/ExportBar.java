package org.issg.ibis.perspective.shared;

import org.issg.ibis.perspective.shared.ExportLink.ExportType;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

public class ExportBar extends VerticalLayout {

	private ExportLink excelLink = new ExportLink(ExportType.XLSX);
	private ExportLink csvLink = new ExportLink(ExportType.CSV);

	public ExportBar() {
		addStyleName("export-bar");
		addComponent(excelLink);
		addComponent(csvLink);
		csvLink.setDescription("Download all tables in CSV format.  This will be provided in a zip archive.");
		excelLink.setDescription("Download all tables in Excel (XLSX) format.");
	}
	
	public void setBaseUrl(String baseUrl) {
		if (!baseUrl.endsWith("/")) {
			baseUrl += '/';
		}
		excelLink.setBaseURL(baseUrl);
		csvLink.setBaseURL(baseUrl);
	}

	public void setResourceId(Long id) {
		csvLink.setResourceId(id);
		excelLink.setResourceId(id);
	}


}