package org.issg.upload;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.issg.ibis.domain.LocationSummary;
import org.issg.ibis.domain.Content;
import org.issg.ibis.domain.ContentType_;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Location_;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesSummary;
import org.issg.ibis.domain.Species_;
import org.jrc.persist.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreatSummaryUploadParser extends UploadParser<Content> {

    private static Logger logger = LoggerFactory
            .getLogger(ThreatSummaryUploadParser.class);

    public ThreatSummaryUploadParser(Dao dao) {
        super(dao, Content.class);
    }

    /**
     * {@inheritDoc}
     */
    public void processWorkbook(Workbook wb) {

        Sheet sheet = wb.getSheetAt(2);
        processSheet(sheet, 0);

    }

    @Override
    protected Content processRow(Row row) {

        String objName = getCellValueAsString(row, 0);
        Species species = dao.findByProxyId(Species_.name, objName);

        if (species != null) {
            System.out.println("processing species " + objName);

            SpeciesSummary speciesSummary = new SpeciesSummary();
            speciesSummary.setSpecies(species);
            getContent(row, speciesSummary);

            return speciesSummary;
        }

        Location location = dao.findByProxyId(Location_.name, objName);
        
        if (location != null) {
            System.out.println("processing location " + objName);
            
            LocationSummary locationSummary = new LocationSummary();
            locationSummary.setLocation(location);

            getContent(row, locationSummary);
            
            return locationSummary;
        }
        
        return null;

    }

    private void getContent(Row row, Content content) {
        content.setContent(getCellValueAsString(row, 1));
        content.setContentType(getEntity(ContentType_.name, row, 2));
    }
}
