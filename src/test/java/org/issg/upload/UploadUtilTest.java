package org.issg.upload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.issg.ibis.IbisUI;
import org.issg.ibis.domain.Country;
import org.issg.ibis.domain.QCountry;
import org.issg.ibis.domain.QSpeciesImpact;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.TestResourceFactory;
import org.issg.ibis.domain.json.GbifSpecies;
import org.jrc.edit.Dao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.inject.Injector;
import com.mysema.query.jpa.impl.JPAQuery;

public class UploadUtilTest {

	private Injector injector = TestResourceFactory.getInjector(IbisUI.class);
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
	public void populateFromJson() {

		List<Species> spp = dao.all(Species.class);
		for (Species species : spp) {
			String j = species.getGbifJson();
			Gson g = new Gson();
			GbifSpecies gsp = g.fromJson(j, GbifSpecies.class);
//			dao.merge(species);
		}
	}
	
	@Test
	public void go() {
		JPAQuery q = new JPAQuery(dao.get());
		
		QSpeciesImpact si = QSpeciesImpact.speciesImpact;
		QCountry c = QCountry.country;
		q = q.from(c, si).where(c.eq(si.location.country));
		q = q.distinct();
		List<Country> results = q.list(c);
		for (Country country : results) {
			System.out.println(country);
		}
	}
	@Test
	public void go2() {
		JPAQuery q = new JPAQuery(dao.get());
		
		QSpeciesImpact si = QSpeciesImpact.speciesImpact;

		QCountry c = QCountry.country;

		List<Country> results = q.list(si.location.country);
		for (Country country : results) {
			System.out.println(country);
		}
	}

	@Test
	public void testWhitespace() {

		SpeciesUploadParser parser = new SpeciesUploadParser(dao);
		boolean isAlpha = parser.isCharAlpha(' ');
		Assert.assertFalse(isAlpha);

		String withNormalWhitespace = " Pterodactylus antiquus ";

		String cleaned = parser.cleanWhitepace(withNormalWhitespace);

		// Should be cleaned
		Assert.assertTrue(parser.isCharAlpha(cleaned.charAt(cleaned.length() - 1)));

		System.out.println("'" + cleaned + "'");

		String withHorribleWhitespace = " Pterodactylus antiquus"
				+ String.valueOf((char) 160);

		String cleanedHorribleWhitespace = parser
				.cleanWhitepace(withHorribleWhitespace);

		Assert.assertTrue(parser.isCharAlpha(cleaned.charAt(cleaned.length() - 1)));

		System.out.println("'" + cleanedHorribleWhitespace + "'");
	}

}
