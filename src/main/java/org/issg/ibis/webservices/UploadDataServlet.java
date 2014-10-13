package org.issg.ibis.webservices;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.issg.ibis.domain.Country;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.QLocation;
import org.issg.ibis.domain.QReference;
import org.issg.ibis.domain.QSpecies;
import org.issg.ibis.domain.Reference;
import org.issg.ibis.domain.Species;
import org.issg.ibis.webservices.writers.WritableSheet;
import org.issg.ibis.webservices.writers.XSSFResultSheet;
import org.jrc.edit.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mysema.query.SearchResults;
import com.mysema.query.jpa.impl.JPAQuery;

@Singleton
public class UploadDataServlet extends HttpServlet {

	private Dao emp;
	private URISlicer uriSlicer;
	private Logger logger = LoggerFactory.getLogger(UploadDataServlet.class);

	@Inject
	public UploadDataServlet(Dao emp, URISlicer uriSlicer) {
		this.emp = emp;
		this.uriSlicer = uriSlicer;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String uri = request.getRequestURI();
		DownloadDescriptor d = uriSlicer.getDownload(uri);

		d.setResponseHeaders(response);
		Long id = d.getEntityId();

		Country c = emp.get().find(Country.class, id);

		Workbook wb = getTemplateWorkbook();

		// Locations
		Sheet sheet = wb.getSheet("masterlist_location");
		writeLocs(sheet, c);

		// Refs
		sheet = wb.getSheet("masterlist_reference");
		writeRefs(sheet);

		// Spp
		sheet = wb.getSheet("species-information");
		writeSpp(sheet);

		ServletOutputStream out = response.getOutputStream();

		if (wb != null) {
			wb.write(out);
		}
		out.flush();
		out.close();
	}

	private void writeRefs(Sheet sheet) {
		List<Reference> refs = emp.all(Reference.class);

		WritableSheet worksheet = new XSSFResultSheet(sheet);
		// ignore the header
		worksheet.setFirstRow(1);
		WorksheetWriter worksheetFactory = new WorksheetWriter();
		
		QReference qR = QReference.reference;
		
		worksheetFactory.addColumn(qR.label);
		worksheetFactory.addColumn(qR.url);
		
		worksheetFactory.writeSheet(worksheet, refs);
	}

	private Workbook getTemplateWorkbook() {
		InputStream stream = this.getClass().getClassLoader()
				.getResourceAsStream("IBIS-template.xlsx");
		Workbook wb;
		try {
			wb = WorkbookFactory.create(stream);
			return wb;
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void writeLocs(Sheet sheet, Country c) {

		QLocation qL = QLocation.location;

		JPAQuery j = new JPAQuery(emp.get());
		SearchResults<Location> x = j.from(qL)
				.where(QLocation.location.country.eq(c)).listResults(qL);
		List<Location> results = x.getResults();

		WritableSheet worksheet = new XSSFResultSheet(sheet);
		// ignore the header
		worksheet.setFirstRow(1);
		WorksheetWriter worksheetFactory = new WorksheetWriter();

		worksheetFactory.setWriteHeader(false);
		// worksheetFactory.addColumn(qL.id);
		worksheetFactory.addColumn(qL.name);
		worksheetFactory.addColumn(qL.country);
		worksheetFactory.addColumn(qL.locationType);
		worksheetFactory.addColumn(qL.islandGroup);
		worksheetFactory.addColumn(qL.latitude);
		worksheetFactory.addColumn(qL.longitude);
		worksheetFactory.addColumn(qL.prefix);
		worksheetFactory.addColumn(qL.identifier);
		worksheetFactory.addColumn(qL.url);
		worksheetFactory.addColumn(qL.area);
		worksheetFactory.addColumn(qL.comments);

		worksheetFactory.writeSheet(worksheet, results);

	}

	private void writeSpp(Sheet sheet) {
			
			List<Species> spp = emp.all(Species.class);
			WritableSheet worksheet = new XSSFResultSheet(sheet);
			worksheet.setFirstRow(1);
			WorksheetWriter worksheetWriter = new WorksheetWriter();
			worksheetWriter.setWriteHeader(false);
	
			QSpecies qS = QSpecies.species;
			
			worksheetWriter.addColumn(qS.uri);
			worksheetWriter.addColumn(qS.name);
			worksheetWriter.addColumn(qS.authority);
			worksheetWriter.addColumn(qS.kingdom);
			worksheetWriter.addColumn(qS.phylum);
			worksheetWriter.addColumn(qS.clazz);
			worksheetWriter.addColumn(qS.order);
			worksheetWriter.addColumn(qS.genus);
			worksheetWriter.addColumn(qS.organismType);
			worksheetWriter.addColumn(qS.commonName);
			worksheetWriter.addColumn(qS.conservationClassification);
			worksheetWriter.addColumn(qS.biomes);
			worksheetWriter.addColumn(qS.gisdLink);
	
			worksheetWriter.writeSheet(worksheet, spp);
		}

}
