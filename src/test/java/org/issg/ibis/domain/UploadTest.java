package org.issg.ibis.domain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.issg.ibis.AppUI;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.upload.BaseLocationUploadParser;
import org.issg.upload.ReferenceUploadParser;
import org.issg.upload.SpeciesImpactUploadParser;
import org.issg.upload.SpeciesLocationUploadParser;
import org.issg.upload.SpeciesUploadParser;
import org.issg.upload.ThreatSummaryUploadParser;
import org.jrc.persist.Dao;
import org.junit.Assert;
import org.junit.Before;

import com.google.inject.Injector;
import com.mysema.query.jpa.JPQLQuery;

public class UploadTest {

    private Injector injector = TestResourceFactory.getInjector(AppUI.class);
    // private EntityManagerFactory emf = injector
    // .getInstance(EntityManagerFactory.class);
    private Workbook workbookGood;
    private Dao dao;
    private EntityManagerFactory emf;

    @Before
    public void init() throws InvalidFormatException, FileNotFoundException,
            IOException {

        // String wbName = "Nov30/Kiribati-November30.xlsx";
        // String wbName = "Nov30/Cook-Islands-November30.xlsx";
        String wbName = "Nov30/Timor_Leste-November30.xlsx";
        // String wbName = "Nov30/Fiji-November30-Revised-Snails.xlsx";

        this.workbookGood = WorkbookFactory.create(TestResourceFactory
                .getFileInputStream(wbName));

        dao = injector.getInstance(Dao.class);
        
        emf = injector.getInstance(EntityManagerFactory.class);
    }

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

    public void speciesLocation() {

        SpeciesLocationUploadParser parser = new SpeciesLocationUploadParser(
                dao);

        parser.processWorkbook(workbookGood);

        List<String> l = parser.getErrors();
        for (String string : l) {
            System.out.println(string);
        }

        Assert.assertFalse(parser.hasErrors());

        List<SpeciesLocation> sls = parser.getEntityList();
        for (SpeciesLocation sl : sls) {
            dao.persist(sl);
        }
    }

    public void speciesImpact() throws IOException {

        SpeciesImpactUploadParser parser = new SpeciesImpactUploadParser(dao);

        parser.processWorkbook(workbookGood);

        for (String err : parser.getErrors()) {
            System.out.println(err);
        }

        Assert.assertFalse(parser.hasErrors());

        List<SpeciesImpact> sis = parser.getEntityList();
        for (SpeciesImpact speciesImpact : sis) {
            dao.persist(speciesImpact);
        }

    }

    public void speciesSummaries() {
        EntityManager em = dao.getEntityManager();

        ThreatSummaryUploadParser parser = new ThreatSummaryUploadParser(dao);
        parser.allowSkippedRows(true);
        parser.processWorkbook(workbookGood);

        for (String err : parser.getErrors()) {
            System.out.println(err);
        }

        Assert.assertFalse(parser.hasErrors());

        List<Content> sls = parser.getEntityList();
        em.getTransaction().begin();
        for (Content ss : sls) {
            em.persist(ss);
        }
        em.getTransaction().commit();

    }

    public void references() {

        EntityManager em = dao.getEntityManager();
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
