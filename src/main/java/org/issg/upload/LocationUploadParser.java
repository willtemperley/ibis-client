package org.issg.upload;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.LocationType;
import org.issg.ibis.domain.LocationType_;
import org.jrc.persist.Dao;
import org.jrc.persist.adminunits.Country;
import org.jrc.persist.adminunits.Country_;

public class LocationUploadParser extends UploadParser<Location> {


    public LocationUploadParser(Dao dao) {
        super(dao, Location.class);
    }

    /**
     * @param wb
     *            The spreadsheet
     */
    public void processWorkbook(Workbook wb) {

        Sheet sheet = wb.getSheetAt(0);
        processSheet(sheet, 0);
    }

    @Override
    protected Location processRow(Row row) {
        
        /*
         * COUNTRY_ISOCODE  0 A
         * SITE_NAME        1 B    
         * SITE_TYPE        2 C
         * PREFIX           3 D
         * IDENTIFIER_SITE  4 E
         * SYSTEM           5 F
         * SIZE_HA          6 G
         * LAT              7 H
         * LONG             8 I
         * LINK             9 J
         * REFERENCE        10 J
         * NOTES            11 K
         * 
         **************************
         *
         * CountryName  0   A
         * Prefix       1   B
         * Identifier   2   C
         * SiteName     3   D
         * Designation/Area of Interest    4   E
         * System       5   F
         * area (ha)    6   G
         * Latitude     7   H
         * Longitude    8   I
         * URL          9  J
         * Comments     10  K
         * IUCN Cat     11  L
         * 
         */

        Location location = new Location();
        

        {
            Country country = getEntity(Country_.name, row, 0);
            location.setCountry(country);
        }
        
        {
            // TODO copying over information from database
        }
        
        {
            String name = getCellValueAsString(row, 3);
            location.setName(name);
        }
        
        {
//            LocationType locationType = getEntity(LocationType_.label, row, 4);
//            location.setLocationType(locationType);
        }
        
        return location;

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
        
        int LOCATION_NAME_COL_IDX = 1;
        
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
