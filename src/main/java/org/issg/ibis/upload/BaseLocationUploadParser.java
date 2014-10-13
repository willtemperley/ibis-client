package org.issg.ibis.upload;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.poi.ss.usermodel.Row;
import org.issg.ibis.domain.Country;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.QCountry;
import org.issg.ibis.domain.QLocation;
import org.issg.ibis.domain.SpeciesLocation;
import org.jrc.edit.Dao;

public abstract class BaseLocationUploadParser<T> extends
        UploadParser<T> {


    public BaseLocationUploadParser(Dao dao) {
        super(dao);
    }
    

    protected Location populateLocation(Row row) {
        Country c = dao.findByQProxyId(QCountry.country, QCountry.country.isoa3Id,
                getCellValueAsString(row, 1));
        
        if (c == null) {
            recordError(row.getRowNum(), 1, "Could not find country.");
            return null;
        }
    
        Location loc = null;
    
        // 1. GID entries
        String gid = getCellValueAsString(row, 6);
        if (gid != null && !gid.isEmpty()) {
    
            String name = "Unknown";
            String islandName = getCellValueAsString(row, 4);
            if (islandName != null && !islandName.isEmpty()) {
                name = islandName;
            } else {
                name = getCellValueAsString(row, 3);
            }
    
            loc = getLocation("GID", gid, name, c.getId());
    
            if (loc == null) {
                recordError(row.getRowNum(), 6, "Could not look up GID:" + gid);
            } else {
                return loc;
            }
        }
    
        // 2. Designated area entries
        if (loc == null) {
            String desAreaName = getCellValueAsString(row, 7);
            String desAreaPrefix = getCellValueAsString(row, 8);
            String desAreaId = getCellValueAsString(row, 9);
            if (desAreaId != null && (!desAreaId.isEmpty())) {
                loc = getLocation(desAreaPrefix, desAreaId,
                        desAreaName, c.getId());
                if (loc == null) {
                    recordError(row.getRowNum(), 9, "Could not look up "
                            + desAreaPrefix + ":" + desAreaId);
                } else {
                    return loc;
                }
            }
        }
    
        // 3. Perhaps we have to look up an island
        if (loc == null) {
            String islandName = getCellValueAsString(row, 4);
    
            if (islandName != null && (!islandName.isEmpty())) {
                loc = dao.findByQProxyId(QLocation.location, QLocation.location.name, islandName);
                if (loc == null) {
                    recordError(row.getRowNum(), 4,
                            "Could not look up island with name: " + islandName);
                } else {
                    return loc;
                }
            }
        }
    
        // 4. Perhaps we have to look up an atoll
        if (loc == null) {
            String atollName = getCellValueAsString(row, 3);
            if (atollName != null && (!atollName.isEmpty())) {
                loc = dao.findByQProxyId(QLocation.location, QLocation.location.name, atollName);
                
                if (loc == null) {
                    recordError(row.getRowNum(), 3, "Could not look up atoll with name: " + atollName);
                } else {
                    return loc;
                }
           } 
        }
    
        // 5. So no location information -- need to look up a country
        if (loc == null) {
            String countryID = getCellValueAsString(row, 1);
            loc = dao.findByQProxyId(QLocation.location, QLocation.location.identifier, countryID);
        }
        
        if (loc == null) {
            recordError(row.getRowNum(), 1, "Could not even find the country.");
        }
        
        return loc;
    }



    /**
     * 
     * Gets a location already in DB or one that's copied from other datasets
     * 
     */
    protected Location getLocation(String prefix, String identifier, String name,
            Long countryId) {

        if (identifier == null) {
            return null;
        }

        if (name == null) {
            name = "";
        }

        if (countryId == null) {
            return null;
        }

        String lookupValue = prefix + ":" + identifier;

//         System.out.println(lookupValue);

        EntityManager em = dao.get();
        Query query = em.createNamedQuery("Location.copy_location")
                .setParameter("full_id", lookupValue)
                .setParameter("name", name)
                .setParameter("country_id", countryId.intValue());

        Location loc = (Location) query.getSingleResult();

        return loc;
    }

}