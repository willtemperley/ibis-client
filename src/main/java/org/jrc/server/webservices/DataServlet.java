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

import org.issg.ibis.domain.QSpeciesImpact;
import org.issg.ibis.domain.SpeciesImpact;
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

	     response.setHeader("Content-Disposition", 
	    "attachment; filename=sampleName.xlsx");
	     
	     List<String> headers = new ArrayList<String>();
	     headers.add("1");
	     headers.add("2");
	     //Use dynabean stuff?
        
        ExcelWriter e = new ExcelWriter(headers);
        
        JPAQuery j = new JPAQuery(emp.get());
        SearchResults<SpeciesImpact> x = 
        		j.from(QSpeciesImpact.speciesImpact).where(QSpeciesImpact.speciesImpact.threatenedSpecies.id.eq(id)).listResults(QSpeciesImpact.speciesImpact);
        List<SpeciesImpact> l = x.getResults();
        
		for (SpeciesImpact si :l) {
			e.writeRow(si);
		}
		
        ServletOutputStream out = response.getOutputStream();
		e.write(out);
		out.flush();
		out.close();

    }
}
