package org.issg.ibis.domain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.biopama.IbisUIBiopama;
import org.issg.ibis.IbisUI;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.upload.*;
import org.jrc.edit.Dao;
import org.junit.Assert;
import org.junit.Before;

import com.google.inject.Injector;
import org.junit.Test;

public class UploadTest {

    private Injector injector = TestResourceFactory.getInjector(IbisUIBiopama.class);
    // private EntityManagerFactory emf = injector
    // .getInstance(EntityManagerFactory.class);
    private Workbook workbookGood;
    private Dao dao;
    private EntityManagerFactory emf;

//    @Before
    public void init() throws InvalidFormatException, FileNotFoundException,
            IOException {
        // String wbName = "Nov30/Kiribati-November30.xlsx";
        // String wbName = "Nov30/Cook-Islands-November30.xlsx";
//        String wbName = "Nov30/Timor_Leste-November30.xlsx";
        // String wbName = "Nov30/Fiji-November30-Revised-Snails.xlsx";
//        String wbName = "master_species-May-2015.xlsx";
//        String wbName =  "master-location-May-12-2015-corrected.xlsx";
        String wbName = //"species-location-impact-May-12-2015.xlsx";
"species-location-impact-20052015.xlsx";

        this.workbookGood = WorkbookFactory.create(TestResourceFactory
                .getFileInputStream(wbName));

        dao = injector.getInstance(Dao.class);
        
        emf = injector.getInstance(EntityManagerFactory.class);
    }

//    @Test
    public void species() throws IOException {

        SpeciesUploadParser parser = new SpeciesUploadParser(dao);
        parser.allowSkippedRows(true);

        parser.processWorkbook(workbookGood);

        for (String err : parser.getErrors()) {
            System.out.println(err);
        }

        Assert.assertFalse(parser.hasErrors());

        List<Species> sis = parser.getEntityList();
        for (Species species : sis) {
            dao.persist(species);
        }

    }

//    @Test
    public void speciesLocation() {

        SpeciesLocationUploadParser parser = new SpeciesLocationUploadParser(
                dao);

        parser.processWorkbook(workbookGood);

        List<String> errors = parser.getErrors();
        printUniqueErrors(errors);

        Assert.assertFalse(parser.hasErrors());

        List<SpeciesLocation> sls = parser.getEntityList();
        for (SpeciesLocation sl : sls) {
            dao.persist(sl);
        }
    }

//    @Test
    public void location() {
        LocationUploadParser parser = new LocationUploadParser(dao);
        parser.processWorkbook(workbookGood);

        for (String err : parser.getErrors()) {
            System.out.println(err);
        }

        Assert.assertFalse(parser.hasErrors());

        List<Location> locations = parser.getEntityList();
        for (Location loc : locations) {
            dao.persist(loc);
        }
    }

//    @Test
    public void speciesImpact() throws IOException {

        SpeciesImpactUploadParser parser = new SpeciesImpactUploadParser(dao);

        parser.processWorkbook(workbookGood);

        List<String> errors = parser.getErrors();

        printUniqueErrors(errors);

        Assert.assertFalse(parser.hasErrors());

        List<SpeciesImpact> sis = parser.getEntityList();
        for (SpeciesImpact speciesImpact : sis) {
            dao.persist(speciesImpact);
        }

    }

    /**
     * As many errors tend to be repeated, the main problems are printed
     *
     * @param errors
     */
    private void printUniqueErrors(List<String> errors) {
        Set<String> s = new HashSet<String>();
        for (String string : errors) {
            String[] x = string.split(",");
            if (x.length == 2) {
                s.add(x[1]);
            }
            if (x.length == 3) {
                s.add(x[1] + x[2]);
            }
        }

        for (String string : s) {
            System.out.println(string);
        }
    }

    public void references() {

        EntityManager em = dao.get();
        ReferenceUploadParser parser = new ReferenceUploadParser(dao);
        parser.processWorkbook(workbookGood);

        for (String err : parser.getErrors()) {
            System.out.println(err);
        }

        Assert.assertFalse(parser.hasErrors());

        List<Reference> refs = parser.getEntityList();
        em.getTransaction().begin();
        for (Reference ref : refs) {
            em.persist(ref);
        }
        em.getTransaction().commit();

    }
}
