package org.issg.upload;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.issg.ibis.domain.BiologicalStatus;
import org.issg.ibis.domain.BiologicalStatus_;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesLocation;
import org.issg.ibis.domain.Species_;
import org.jrc.edit.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeciesLocationUploadParser extends BaseLocationUploadParser<SpeciesLocation> {

    
    private static Logger logger = LoggerFactory
            .getLogger(SpeciesLocationUploadParser.class);

    public SpeciesLocationUploadParser(Dao dao) {
        super(dao, SpeciesLocation.class);

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
    protected SpeciesLocation processRow(Row row) {

        // Quick hack to avoid processing blank rows
        String proxy = getCellValueAsString(row, 1);
        if (proxy == null) {
            return null;
        }
        
        SpeciesLocation speciesLocation = new SpeciesLocation();
        {
            Species sp = getEntity(Species_.name, row, 0);
            speciesLocation.setSpecies(sp);
        }

        //Location information
        Location loc = populateLocation(row);
        speciesLocation.setLocation(loc);

        // Biological status
        BiologicalStatus bioStatus = getEntity(BiologicalStatus_.label, row, 10);
        speciesLocation.setBiologicalStatus(bioStatus);

        return speciesLocation;

    }

//    protected void populateLocation(Row row, SpeciesLocation speciesLocation) {
//        Country c = dao.findByProxyId(Country_.isoa3Id,
//                getCellValueAsString(row, 1));
//
//        // 1. GID entries
//        String gid = getCellValueAsString(row, 6);
//        if (gid != null && !gid.isEmpty()) {
//
//            String name = "Unknown";
//            String islandName = getCellValueAsString(row, 4);
//            if (islandName != null && !islandName.isEmpty()) {
//                name = islandName;
//            } else {
//                name = getCellValueAsString(row, 3);
//            }
//
//            Location loc = getLocation("GID", gid, name, c.getId());
//
//            speciesLocation.setLocation(loc);
//            if (loc == null) {
//                recordError(row.getRowNum(), 6, "Could not look up GID:" + gid);
//            }
//        }
//
//        // 2. Designated area entries
//        if (speciesLocation.getLocation() == null) {
//            String desAreaName = getCellValueAsString(row, 7);
//            String desAreaPrefix = getCellValueAsString(row, 8);
//            String desAreaId = getCellValueAsString(row, 9);
//            if (desAreaId != null && (!desAreaId.isEmpty())) {
//                Location loc = getLocation(desAreaPrefix, desAreaId,
//                        desAreaName, c.getId());
//                speciesLocation.setLocation(loc);
//                if (loc == null) {
//                    recordError(row.getRowNum(), 9, "Could not look up "
//                            + desAreaPrefix + ":" + desAreaId);
//                }
//            }
//        }
//
//        // 3. Perhaps we have to look up an island
//        if (speciesLocation.getLocation() == null) {
//            String islandName = getCellValueAsString(row, 4);
//
//            if (islandName != null && (!islandName.isEmpty())) {
//                Location loc = dao.findByProxyId(Location_.name, islandName);
//                if (loc == null) {
//                    recordError(row.getRowNum(), 4,
//                            "Could not look up island with name: " + islandName);
//                }
//                speciesLocation.setLocation(loc);
//            }
//        }
//
//        // 4. Perhaps we have to look up an atoll
//        if (speciesLocation.getLocation() == null) {
//            String atollName = getCellValueAsString(row, 3);
//            Location loc = dao.findByProxyId(Location_.name, atollName);
//
//            if (atollName != null && (!atollName.isEmpty()) && loc == null) {
//                recordError("Could not look up atoll with name: " + atollName);
//            }
//
//            speciesLocation.setLocation(loc);
//        }
//
//        // 5. So no location information -- need to look up a country
//        if (speciesLocation.getLocation() == null) {
//            String countryID = getCellValueAsString(row, 1);
//            Location l = dao.findByProxyId(Location_.identifier, countryID);
//            speciesLocation.setLocation(l);
//        }
//    }

}
