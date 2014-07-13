package org.issg.ibis.webservices;

import java.util.List;

import javax.servlet.http.HttpServletResponse;


public class DownloadDescriptor {

	public String getEntityType() {
		return entityType;
	}

	private String entityType;

	public String getFacetType() {
		return facetType;
	}

	private String facetType;

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

	public DownloadDescriptor(List<String> parts) {

//		Preconditions.checkArgument(parts.size() == 4);

		entityType = parts.get(0);
		entityId = Long.valueOf(parts.get(1));
		facetType = parts.get(2);
		format = parts.get(3);

	}

	public boolean isValid() {
		return (entityId != null && format != null && facetType != null && entityType != null);
	}

	public void setResponseInfo(HttpServletResponse response) {

		if (format.equals("xlsx")) {
			response.setContentType(XLSX);
		} else if (format.equals("csv")) {
			response.setContentType(CSV);
		}

		String fileName = getEntityType() + "_" + getEntityId();
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName + "." + format);

	}

}