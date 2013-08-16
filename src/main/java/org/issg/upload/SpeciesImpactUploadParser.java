package org.issg.upload;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.issg.ibis.domain.ImpactMechanism;
import org.issg.ibis.domain.ImpactMechanism_;
import org.issg.ibis.domain.ImpactOutcome;
import org.issg.ibis.domain.ImpactOutcome_;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Location_;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.Species_;
import org.jrc.persist.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeciesImpactUploadParser extends UploadParser<SpeciesImpact> {

    private static Logger logger = LoggerFactory
            .getLogger(SpeciesImpactUploadParser.class);
    

    public SpeciesImpactUploadParser(Dao dao) {
        super(dao, SpeciesImpact.class);

    }

    /**
     * @param wb
     *            The spreadsheet
     */
    public void processWorkbook(Workbook wb) {

        Sheet sheet = wb.getSheetAt(3);
        processSheet(sheet, 0);
    }
    

    @Override
    protected SpeciesImpact processRow(Row row) {

        //Quick hack to avoid processing blank rows
        String proxy = getCellValueAsString(row, 1);
        if (proxy == null) {
            return null;
        }

        SpeciesImpact speciesImpact = new SpeciesImpact();

        {
            // Threatened species
            Species sp = getEntity(Species_.name, row, 1);
            speciesImpact.setThreatenedSpecies(sp);
        }

        {
            // Location 
            // FIXME: Need multiple prefixes
            String lookup = "GID:" + getCellValueAsString(row, 3);
            Location loc = getEntity(Location_.locationId, row, 3, lookup);
            speciesImpact.setLocation(loc);
        }

        {
            // Invasive species
            Species sp = getEntity(Species_.name, row, 4);
            speciesImpact.setInvasiveSpecies(sp);
        }

        {
            // Impact mechanism
            ImpactMechanism im = getEntity(ImpactMechanism_.label, row, 5);
            speciesImpact.setImpactMechanism(im);
        }

        {
            // Impact outcome
            ImpactOutcome io = getEntity(ImpactOutcome_.label, row, 6);
            speciesImpact.setImpactOutcome(io);
        }
        
        return speciesImpact;

    }
}
