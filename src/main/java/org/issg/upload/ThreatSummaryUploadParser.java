package org.issg.upload;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.Species_;
import org.jrc.persist.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreatSummaryUploadParser extends UploadParser<Species> {

    private static Logger logger = LoggerFactory
            .getLogger(ThreatSummaryUploadParser.class);

    public ThreatSummaryUploadParser(Dao dao) {
        super(dao, Species.class);
    }

    /**
     * {@inheritDoc}
     */
    public void processWorkbook(Workbook wb) {

        Sheet sheet = wb.getSheetAt(2);
        processSheet(sheet, 0);

    }

    @Override
    protected Species processRow(Row row) {

        Species species = getEntity(Species_.name, row, 1);

        species.setThreatSummary(getCellValueAsString(row, 4));

        species.setManagementSummary(getCellValueAsString(row, 5));

        species.setConservationOutcomes(getCellValueAsString(row, 6));

        return species;
    }
}
