package org.biopama.ibis.upload;

import java.util.HashSet;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.biopama.edit.Dao;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.QLocation;
import org.issg.ibis.domain.QReference;
import org.issg.ibis.domain.QSpecies;
import org.issg.ibis.domain.Reference;
import org.issg.ibis.domain.Species;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeciesReferenceUploadParser extends UploadParser<Species> {

	private static Logger logger = LoggerFactory
			.getLogger(SpeciesReferenceUploadParser.class);

	public SpeciesReferenceUploadParser(Dao dao) {
		super(dao);
	}

	/**
	 * {@inheritDoc}
	 */
	public void processWorkbook(Workbook wb) {

		Sheet sheet = wb.getSheetAt(1);
		processSheet(sheet, 0);

	}

	@Override
	protected Species processRow(Row row) {

		Species sp = getEntity(QSpecies.species, QSpecies.species.name, row, 1);

		String refList = getCellValueAsString(row, 2);

		String[] refIds = refList.split(",");

		Set<Reference> refs = sp.getReferences();
//		Set<Reference> refs = new HashSet<Reference>();
		for (int i = 0; i < refIds.length; i++) {
			try {
				String s = refIds[i].trim();
				if (!s.isEmpty()) {
					Long refId = Long.valueOf(s);
					Reference ref = dao.find(Reference.class, refId);
					refs.add(ref);
				}
			} catch (NumberFormatException e) {
				recordError("issues with " + row.getRowNum() + " reflist :"
						+ refList);
			}

		}
//		sp.setReferences(refs);

		return sp;

	}

}
