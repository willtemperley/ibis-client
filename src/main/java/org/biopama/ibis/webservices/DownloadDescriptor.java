package org.biopama.ibis.webservices;

import java.util.List;

import javax.servlet.http.HttpServletResponse;


public class DownloadDescriptor {

	public String getEntityType() {
		return entityType;
	}

	private String entityType;

	public Long getEntityId() {
		return entityId;
	}

	private Long entityId;

	public String getFormat() {
		return format;
	}

	private String format;

	private static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	private static final String CSV = "text/csv";
	private static final String ZIP = "application/zip";

	public DownloadDescriptor(List<String> parts) {

		entityType = parts.get(0);
		entityId = Long.valueOf(parts.get(1));
		format = parts.get(2);

	}

	public boolean isValid() {
		return (entityId != null && format != null && entityType != null);
	}

	public void setResponseHeaders(HttpServletResponse response) {

		String outputFormat = format;
		if (format.equals("xlsx")) {
			response.setContentType(XLSX);
		} else if (format.equals("csv")) {
			response.setContentType(ZIP);
			outputFormat = "zip";
		}

		String fileName = getEntityType() + "_" + getEntityId();
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName + "." + outputFormat);

	}

}