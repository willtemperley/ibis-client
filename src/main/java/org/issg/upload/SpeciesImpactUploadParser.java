package org.issg.upload;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.issg.ibis.domain.ImpactMechanism;
import org.issg.ibis.domain.ImpactMechanism_;
import org.issg.ibis.domain.ImpactOutcome;
import org.issg.ibis.domain.ImpactOutcome_;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.Species_;
import org.jrc.persist.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeciesImpactUploadParser extends UploadParser<SpeciesImpact> {

    private static final int LOCATION_NAME_COL_IDX = 1;
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

        // Quick hack to avoid processing blank rows
        String proxy = getCellValueAsString(row, 1);
        if (proxy == null) {
            return null;
        }

        SpeciesImpact speciesImpact = new SpeciesImpact();

        // WDPA entries
        String wdpaId = getCellValueAsString(row, 2);
        if (wdpaId != null && !wdpaId.isEmpty()) {
            Location loc = processLocation(row, "WDPA", 2);
            speciesImpact.setLocation(loc);
        }
        
        // GID entries
        String gid = getCellValueAsString(row, 3);
        if (gid != null && !gid.isEmpty()) {
            Location loc = processLocation(row, "GID", 3);
            speciesImpact.setLocation(loc);
        }

        {
            // Threatened species
            Species sp = getEntity(Species_.name, row, 4);
            speciesImpact.setThreatenedSpecies(sp);
        }

        {
            // Invasive species
            Species sp = getEntity(Species_.name, row, 5);
            speciesImpact.setInvasiveSpecies(sp);
        }

        {
            // Impact mechanism
            ImpactMechanism im = getEntity(ImpactMechanism_.label, row, 6);
            speciesImpact.setImpactMechanism(im);
        }

        {
            // Impact outcome
            ImpactOutcome io = getEntity(ImpactOutcome_.label, row, 7);
            speciesImpact.setImpactOutcome(io);
        }

        return speciesImpact;

    }

    /**
     * Gets a location
     * 
     * @param row
     * @param nameSpace
     * @param colIdx
     * @param speciesImpact
     * @return 
     */
    private Location processLocation(Row row, String nameSpace, int colIdx) {
        
        String locationName = getCellValueAsString(row, LOCATION_NAME_COL_IDX);
        
        String locationId = getCellValueAsString(row, colIdx);
        if (locationId != null && !locationId.isEmpty()) {

            EntityManager em = dao.getEntityManager();

            Integer locationIdInt = null;
            try {
                locationIdInt = Integer.valueOf(locationId);
            } catch (NumberFormatException e) {
                recordError(row.getRowNum(), 2, "Invalid integer: "
                        + locationId);
            }

            Location loc = null;

            if (locationIdInt != null) {

                Query query = em.createNamedQuery("Location.copy_location")
                        .setParameter("namespace", nameSpace)
                        .setParameter("id", locationIdInt);

                // System.out.println(nameSpace);
                // System.out.println(locationIdInt);
                loc = (Location) query.getSingleResult();
            }

            if (loc != null) {
                if (locationName != null && !locationName.isEmpty()) {
                    loc.setName(locationName);
                    dao.persist(loc);
                }
            } else {
                recordError(row.getRowNum(), 2,
                        "Couldn't find location with namespace: "
                                + nameSpace + " id: " + locationIdInt);
            }
            return loc;
        }
        return null;
    }
}
