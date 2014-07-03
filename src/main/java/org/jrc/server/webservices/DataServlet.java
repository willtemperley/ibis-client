package org.jrc.server.webservices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.issg.ibis.domain.QSpeciesImpact;
import org.issg.ibis.domain.QSpeciesLocation;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.SpeciesLocation;
import org.jrc.persist.Dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mysema.query.SearchResults;
import com.mysema.query.jpa.impl.JPAQuery;

@Singleton
public class DataServlet extends HttpServlet {
	
	private Provider<EntityManager> emp;

	@Inject
	public DataServlet(Provider<EntityManager> emp) {
		this.emp = emp;
	}

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        String sid = request.getParameter("id");
        Long id = Long.valueOf(sid);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		XSSFWorkbook workbook = new XSSFWorkbook();

	     response.setHeader("Content-Disposition", 
	    "attachment; filename=sampleName.xlsx");
        
        writeSpeciesImpacts(id, workbook);
        writeLocations(id, workbook);
		
        ServletOutputStream out = response.getOutputStream();
		workbook.write(out);

		out.flush();
		out.close();

    }

	private void writeSpeciesImpacts(Long id, XSSFWorkbook workbook) {
		WorksheetFactory e = new WorksheetFactory();
        XSSFSheet worksheet = workbook.createSheet("Impacts");
        QSpeciesImpact speciesimpact = QSpeciesImpact.speciesImpact;
        e.addColumn(speciesimpact.invasiveSpecies);
		e.addColumn(speciesimpact.impactMechanism);
		e.addColumn(speciesimpact.impactOutcome);
        e.addColumn(speciesimpact.nativeSpecies);
        e.addColumn(speciesimpact.location);
        
        JPAQuery j = new JPAQuery(emp.get());
        SearchResults<SpeciesImpact> x = j.from(speciesimpact).where(speciesimpact.nativeSpecies.id.eq(id)).listResults(speciesimpact);
        List<SpeciesImpact> results = x.getResults();
        
        e.writeSheet(worksheet, results);
	}

	private void writeLocations(Long id, XSSFWorkbook workbook) {
		WorksheetFactory e = new WorksheetFactory();
	    XSSFSheet worksheet = workbook.createSheet("Locations");
	    QSpeciesLocation entityPath = QSpeciesLocation.speciesLocation;
	    e.addColumn(entityPath.location);
		e.addColumn(entityPath.species);
	    
	    JPAQuery j = new JPAQuery(emp.get());
	    SearchResults<SpeciesLocation> x = j.from(entityPath).where(entityPath.species.id.eq(id)).listResults(entityPath);
	    List<SpeciesLocation> results = x.getResults();
	    
	    e.writeSheet(worksheet, results);
	}
}
