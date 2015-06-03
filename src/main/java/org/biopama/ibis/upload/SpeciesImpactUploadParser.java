package org.biopama.ibis.upload;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.biopama.edit.Dao;
import org.issg.ibis.domain.ImpactMechanism;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.QImpactMechanism;
import org.issg.ibis.domain.QLocation;
import org.issg.ibis.domain.QSpecies;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeciesImpactUploadParser extends BaseLocationUploadParser<SpeciesImpact> {

    private static Logger logger = LoggerFactory
            .getLogger(SpeciesImpactUploadParser.class);

    public SpeciesImpactUploadParser(Dao dao) {
        super(dao);
    }

    /**
     * @param wb
     *            The spreadsheet
     */
    public void processWorkbook(Workbook wb) {

        Sheet sheet = wb.getSheetAt(1);

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
            // Native species
            Species sp = getEntity(QSpecies.species, QSpecies.species.name, row, 1);
            speciesImpact.setNativeSpecies(sp);
        }
        {
            // Invasive species
            Species sp = getEntity(QSpecies.species, QSpecies.species.name, row, 2);
            speciesImpact.setInvasiveSpecies(sp);
        }

        //Location information
        {
            Location loc = getEntity(QLocation.location, QLocation.location.name, row, 3);
            speciesImpact.setLocation(loc);
        }

        {
            // Impact mechanism
            ImpactMechanism im = getEntity(QImpactMechanism.impactMechanism, QImpactMechanism.impactMechanism.label, row, 4);
            speciesImpact.setImpactMechanism(im);
        }

//        {
//            // Biological status
////            String biostatus = getCellValueAsString(row, 10);
//            BiologicalStatus bioStatus = getEntity(QBiologicalStatus.biologicalStatus, QBiologicalStatus.biologicalStatus.label, row, 10);
//            speciesImpact.setBiologicalStatus(bioStatus);
//        }

        return speciesImpact;

    }

    /**
     * 
     * Gets a location already in DB or one that's copied from other datasets
     * 
     */
    private Location getLocation(String prefix, String identifier) {

        EntityManager em = dao.get();
        Query query = em.createNamedQuery("Location.copy_location")
                .setParameter("full_id", prefix + ":" + identifier)
        ;

        Location loc = (Location) query.getSingleResult();
        
        return loc;
    }
}
