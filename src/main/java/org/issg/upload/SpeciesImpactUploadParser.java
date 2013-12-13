package org.issg.upload;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.issg.ibis.domain.BiologicalStatus;
import org.issg.ibis.domain.BiologicalStatus_;
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

public class SpeciesImpactUploadParser extends BaseLocationUploadParser<SpeciesImpact> {

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

        Sheet sheet = wb.getSheetAt(2);

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

        {
            // Threatened species
            Species sp = getEntity(Species_.name, row, 0);
            speciesImpact.setThreatenedSpecies(sp);
        }

        //Location information
        Location loc = populateLocation(row);
        speciesImpact.setLocation(loc);


        {
            // Invasive species
            Species sp = getEntity(Species_.name, row, 11);
            speciesImpact.setInvasiveSpecies(sp);
        }

        {
            // Impact mechanism
            ImpactMechanism im = getEntity(ImpactMechanism_.label, row, 13);
            speciesImpact.setImpactMechanism(im);
        }

        {
            // Impact outcome
            ImpactOutcome io = getEntity(ImpactOutcome_.label, row, 14);
            speciesImpact.setImpactOutcome(io);
        }

        {
            // Biological status
//            String biostatus = getCellValueAsString(row, 10);
            BiologicalStatus bioStatus = getEntity(BiologicalStatus_.label, row, 10);
            speciesImpact.setBiologicalStatus(bioStatus);
        }

        return speciesImpact;

    }

    /**
     * 
     * Gets a location already in DB or one that's copied from other datasets
     * 
     */
    private Location getLocation(String prefix, String identifier) {

        EntityManager em = dao.getEntityManager();
        Query query = em.createNamedQuery("Location.copy_location")
                .setParameter("full_id", prefix + ":" + identifier)
        ;

        Location loc = (Location) query.getSingleResult();
        
        return loc;
    }
}
