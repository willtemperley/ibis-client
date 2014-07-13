package org.issg.ibis.webservices;

import java.util.List;

import com.google.common.base.Splitter;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class URISlicer {

	private String contextPath;

	@Inject
	public URISlicer(@Named("context_path") String contextPath) {
		this.contextPath = contextPath;
		if (contextPath == null) {
			contextPath = "";
		}
		this.contextPath = contextPath + "/download/";
	}

	protected List<String> getParts(String uri) {

		
		String rest = uri.substring(contextPath.length(), uri.length());
		if (rest.startsWith("/")) {
			rest = rest.substring(1, rest.length());
		}
		List<String> uriParts = Splitter.on('/').splitToList(rest);
		return uriParts;
	}

	public DownloadDescriptor getDownload(String string) {
		DownloadDescriptor d = new DownloadDescriptor(getParts(string));
		if (d.isValid()) {
			return d;
		}
		return null;
	}

}
