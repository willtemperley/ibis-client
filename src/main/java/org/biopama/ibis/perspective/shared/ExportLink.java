package org.biopama.ibis.perspective.shared;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Link;

public class ExportLink extends Link {

	public enum ExportType {
		CSV, XLSX
	}

	private String baseURL;

	private String typeName;

	public ExportLink(ExportType exportType) {
		this.typeName = exportType.toString().toLowerCase();
		setIcon(new ThemeResource("img/download/" + typeName + "-32.png"));
	}
	
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public void setResourceId(Long resourceId) {

		setResource(new ExternalResource(baseURL + resourceId + "/" + typeName));

	}

}
