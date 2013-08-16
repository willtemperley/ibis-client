package org.issg.ibis.domain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.upload.SpeciesImpactUploadParser;
import org.issg.upload.SpeciesUploadParser;
import org.jrc.persist.Dao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Injector;

public class UploadTest {

    private static final int NumSpecies = 10;
    
    private Injector injector = TestResourceFactory.getInjector();
    private EntityManagerFactory emf = injector
            .getInstance(EntityManagerFactory.class);
    private Workbook workbookGood;
    private Workbook workbookBad;
    private Dao dao;

    @Before
    public void init() throws InvalidFormatException, FileNotFoundException,
            IOException {

        this.workbookGood = WorkbookFactory.create(TestResourceFactory
                .getFileInputStream("Species-Summary-Impact.xlsx"));
        this.workbookBad = WorkbookFactory.create(TestResourceFactory
                .getFileInputStream("Species-Summary-Impact-Mistakes.xlsx"));
        dao = new Dao(null, emf, null, null);
    }

    @Test
    public void speciesImpactZeroErrors() throws IOException {

        SpeciesImpactUploadParser parser = new SpeciesImpactUploadParser(dao);

        parser.processWorkbook(workbookGood);

        for (String err : parser.getErrors()) {
            System.out.println(err);
        }

        Assert.assertFalse(parser.hasErrors());

        List<SpeciesImpact> sis = parser.getEntityList();
        for (SpeciesImpact speciesImpact : sis) {
            // System.out.println("===============================");
            Assert.assertNotNull(speciesImpact.getInvasiveSpecies());
            Assert.assertNotNull(speciesImpact.getThreatenedSpecies());
            Assert.assertNotNull(speciesImpact.getLocation());
            Assert.assertNotNull(speciesImpact.getImpactMechanism());
            Assert.assertNotNull(speciesImpact.getImpactOutcome());
        }

    }

    @Test
    public void speciesImpactErrors() {

        SpeciesImpactUploadParser parser = new SpeciesImpactUploadParser(dao);

        parser.processWorkbook(workbookBad);

        List<String> errors = parser.getErrors();

        Assert.assertTrue(parser.hasErrors());

        for (String string : errors) {
            System.out.println(string);
        }
    }

    @Test
    public void speciesZeroErrors() throws IOException {
    
        SpeciesUploadParser parser = new SpeciesUploadParser(dao);
    
        parser.processWorkbook(workbookGood);
    
        for (String err : parser.getErrors()) {
            System.out.println("=============");
            System.out.println("Errors");
            System.out.println("=============");
            System.out.println(err);
        }
    
    
        List<Species> sis = parser.getEntityList();
        for (Species species : sis) {
            
            System.out.println("=============");
            System.out.println(species.getName());
            System.out.println(species.getAuthority());
            
        }
    
        Assert.assertFalse(parser.hasErrors());
        Assert.assertTrue(sis.size() == NumSpecies);
    }

}
