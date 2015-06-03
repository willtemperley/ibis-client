package org.biopama.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.biopama.edit.Dao;
import org.biopama.ibis.webservices.URISlicer;
import org.issg.ibis.domain.Species;
import org.vaadin.addons.guice.servlet.VGuiceApplicationServlet;
import org.vaadin.addons.guice.ui.ScopedUIProvider;

import com.google.common.base.Splitter;
import com.google.inject.Inject;

@Singleton
public class IBISApplicationServlet extends VGuiceApplicationServlet {

	private Dao dao;

	@Inject
	public IBISApplicationServlet(ScopedUIProvider applicationProvider, Dao dao) {
		super(applicationProvider);
		this.dao = dao;
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String fragment = request.getParameter("_escaped_fragment_");
		if (fragment != null) {
			response.setContentType("text/html");
			PrintWriter writer = response.getWriter();
			
			List<String> uriParts = Splitter.on('/').splitToList(fragment);
			
			writer.append("<html><body>");

			if (uriParts.size() == 2) {
				writeEntity(writer, uriParts.get(0), uriParts.get(1));
			}

			writer.append("</body>");
		} else {
			super.service(request, response);
		}

	}

	private void writeEntity(PrintWriter writer, String root, String stringId) {
		if (root.equals("Species")) {
			Long id = Long.valueOf(stringId);
			Species sp = dao.find(Species.class, id);
			writer.append("<div>" + sp.getName() + "</div>");
		}
	}

}
