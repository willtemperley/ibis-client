package org.issg.ibis.perspective.shared;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

public class ExportBar extends VerticalLayout {

	private Link excelLink = buildDownloadLink("img/download/excel-32.png");
	private Link csvLink = buildDownloadLink("img/download/csv-32.png");
	private String baseUrl;

	public ExportBar() {
		
		addStyleName("export-bar");
		addComponent(excelLink);
		addComponent(csvLink);
	}

	public void setBaseUrl(String baseUrl) {
		if (!baseUrl.endsWith("/")) {
			baseUrl += '/';
		}
		this.baseUrl = baseUrl;
	}

	public void setResourceId(Long id) {
		csvLink.setResource(new ExternalResource(baseUrl + id + "/csv"));
		excelLink.setResource(new ExternalResource(baseUrl + id + "/xlsx"));
		
	}

	private Link buildDownloadLink(String icon) {
		
		ThemeResource resource = new ThemeResource(icon);
		Link link = new Link();
		link.setIcon(resource);
		return link;
	}

}
