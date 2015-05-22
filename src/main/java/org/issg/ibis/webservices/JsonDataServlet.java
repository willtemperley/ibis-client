package org.issg.ibis.webservices;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.issg.ibis.domain.QSpeciesImpact;
import org.issg.ibis.domain.QSpeciesLocation;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.SpeciesLocation;
import org.issg.ibis.webservices.writers.CsvResultWriter;
import org.issg.ibis.webservices.writers.WritableSheet;
import org.issg.ibis.webservices.writers.ResultWriter;
import org.issg.ibis.webservices.writers.XSSFResultWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mysema.query.SearchResults;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;

@Singleton
public class JsonDataServlet extends HttpServlet {

	private Provider<EntityManager> emp;
	private URISlicer uriSlicer;
	private Logger logger = LoggerFactory.getLogger(JsonDataServlet.class);

	@Inject
	public JsonDataServlet(Provider<EntityManager> emp, URISlicer uriSlicer) {
		this.emp = emp;
		this.uriSlicer = uriSlicer;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String uri = request.getRequestURI();

		DownloadDescriptor d = uriSlicer.getDownload(uri);

		d.setResponseHeaders(response);

		ResultWriter writer;

		if (d.getFormat().equals("xlsx")) {
			 writer = new XSSFResultWriter();
		} else if (d.getFormat().equals("csv")) {
			writer = new CsvResultWriter();
		} else {
			logger.error("Unsupported format: " + d.getFormat());
			return;
		}

		writeSpeciesImpacts(writer, "Invasive", QSpeciesImpact.speciesImpact.invasiveSpecies.id.eq(d.getEntityId()));
		writeSpeciesImpacts(writer, "Native", QSpeciesImpact.speciesImpact.nativeSpecies.id.eq(d.getEntityId()));
		writeLocations(d.getEntityId(), writer);

		ServletOutputStream out = response.getOutputStream();
		writer.write(out);

		out.flush();
		out.close();
	}

	private void writeSpeciesImpacts(ResultWriter writer, String type, BooleanExpression eq) {

		QSpeciesImpact speciesimpact = QSpeciesImpact.speciesImpact;

		JPAQuery j = new JPAQuery(emp.get());
		SearchResults<SpeciesImpact> x = j.from(speciesimpact)
				.where(eq)
				.listResults(speciesimpact);
		List<SpeciesImpact> results = x.getResults();
		
		if(x.isEmpty()) {
			return;
		}

		WorksheetWriter worksheetFactory = new WorksheetWriter();

		WritableSheet worksheet = writer.createSheet("SpeciesImpacts");

		if (type.equals("Native")) {
			worksheetFactory.addColumn(speciesimpact.invasiveSpecies);
		} else {
			worksheetFactory.addColumn(speciesimpact.nativeSpecies);
		}
		worksheetFactory.addColumn(speciesimpact.impactMechanism);
//		worksheetFactory.addColumn(speciesimpact.impactOutcome);
		worksheetFactory.addColumn(speciesimpact.location);

		worksheetFactory.writeSheet(worksheet, results);
	}

	private void writeLocations(Long id, ResultWriter writer) {
		WorksheetWriter worksheetFactory = new WorksheetWriter();
		WritableSheet worksheet = writer.createSheet("Locations");
		QSpeciesLocation entityPath = QSpeciesLocation.speciesLocation;
		worksheetFactory.addColumn(entityPath.location);
		worksheetFactory.addColumn(entityPath.species);

		JPAQuery j = new JPAQuery(emp.get());
		SearchResults<SpeciesLocation> x = j.from(entityPath)
				.where(entityPath.species.id.eq(id)).listResults(entityPath);
		List<SpeciesLocation> results = x.getResults();

		worksheetFactory.writeSheet(worksheet, results);
	}
	

}
