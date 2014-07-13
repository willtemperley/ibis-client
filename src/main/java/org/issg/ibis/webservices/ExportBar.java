package org.issg.ibis.webservices;

import org.issg.ibis.domain.Species;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

public class ExportBar extends VerticalLayout {

	private Link excelLink = buildDownloadLink("img/download/excel-32.png");
	private Link csvLink = buildDownloadLink("img/download/csv-32.png");

	public ExportBar() {
		addStyleName("export-bar");
		addComponent(excelLink);
		addComponent(csvLink);
	}

	public void setEntity(Species entity) {
		excelLink.setResource(new ExternalResource("/ibis-client/download/species/" + entity.getId() + "/occurrence/xlsx"));
	}
	
	private Link buildDownloadLink(String icon) {
		
		ThemeResource resource = new ThemeResource(icon);
		Link link = new Link();
		link.setIcon(resource);
		return link;
	}

}
