package org.issg.upload;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.issg.ibis.AppUI;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.TestResourceFactory;
import org.issg.ibis.domain.json.GbifSpecies;
import org.issg.ibis.webservices.GbifApi09;
import org.jrc.persist.Dao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Injector;

public class UploadUtilTest {

    private Injector injector = TestResourceFactory.getInjector(AppUI.class);
    private Workbook workbookGood;
    private Dao dao;

    @Before
    public void init() throws InvalidFormatException, FileNotFoundException,
            IOException {

        String wbName = "Nov30/Kiribati-November30.xlsx";

        this.workbookGood = WorkbookFactory.create(TestResourceFactory
                .getFileInputStream(wbName));

        dao = injector.getInstance(Dao.class);
    }
    
    @Test
    public void test2() {
    	Species s = dao.find(Species.class, 3786l);
    	String t = s.getGbifJson();
//    	GbifApi09.prettyPrint(t);
    	
    	Gson g = new Gson();
    	GbifSpecies z = g.fromJson(t, GbifSpecies.class);

    	System.out.println(z.getKingdom());
    }

    @Test
    public void testWhitespace() {
        
        SpeciesUploadParser parser = new SpeciesUploadParser(dao);
        boolean isAlpha = parser.isCharAlpha(' ');
        Assert.assertFalse(isAlpha);
        
        String withNormalWhitespace = " Pterodactylus antiquus ";
        
        String cleaned = parser.cleanWhitepace(withNormalWhitespace);
        
        //Should be cleaned
        Assert.assertTrue(parser.isCharAlpha(cleaned.charAt(cleaned.length() - 1)));
        

        System.out.println("'" + cleaned + "'");

        String withHorribleWhitespace = " Pterodactylus antiquus" + String.valueOf((char) 160);

        String cleanedHorribleWhitespace = parser.cleanWhitepace(withHorribleWhitespace);

        Assert.assertTrue(parser.isCharAlpha(cleaned.charAt(cleaned.length() - 1)));

        System.out.println("'" + cleanedHorribleWhitespace + "'");
    }


}
