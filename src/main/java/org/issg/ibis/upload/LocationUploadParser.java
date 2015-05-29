package org.issg.ibis.upload;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.biopama.edit.Dao;
import org.issg.ibis.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Created by will on 23/04/15.
 */
public class LocationUploadParser extends UploadParser<Location> {

    /*
location_name
location_country
location_type
island_group
lat
long
area (Ha)
prefix
identifier
url
comments
     */

    public LocationUploadParser(Dao dao) {
        super(dao);
    }

    @Override
    protected Location processRow(final Row row) {

        //Location information
        Location loc = getEntity(QLocation.location, QLocation.location.name, row, 0);
        if (loc == null ) {
            return null;
        }

        String prefix = getCellValueAsString(row, 7);
        loc.setPrefix(prefix);

        String identifier = getCellValueAsString(row, 8);
        loc.setIdentifier(identifier);

        String url = getCellValueAsString(row, 9);
        loc.setUrl(url);

        String comments = getCellValueAsString(row, 10);
        loc.setComments(comments);

        {
            Double lat = extractOrdinate(row, 4);
            loc.setLatitude(lat);
            Double lon = extractOrdinate(row, 5);
            loc.setLongitude(lon);

        }

        {
            String locationName = getCellValueAsString(row, 0);
            loc.setName(locationName);
        }

        {
            String attrValue = getCellValueAsString(row, 1);
            Country c = dao.findByQProxyId(QCountry.country, QCountry.country.name,
                    attrValue);

            loc.setCountry(c);
            if (c == null) {
                recordError(row.getRowNum(), 1, "Could not find country named \"" + attrValue + "\"");
                return loc;
            }
        }

        {
            LocationType lt = dao.findByQProxyId(QLocationType.locationType, QLocationType.locationType.id,
                    getCellValueAsString(row, 2));
            loc.setLocationType(lt);
        }

        {
            loc.setIslandGroup(getCellValueAsString(row, 3));
        }

        return loc;
    }

    private Double extractOrdinate(Row row, int i) {

        try {

            String ordinate = getCellValueAsString(row, i);
            if (ordinate != null && !ordinate.isEmpty()) {
                ordinate = ordinate.replace("°", "");
                return Double.valueOf(ordinate);
            }

        } catch (Exception e) {
            String bad = getCellValueAsString(row, i);
            recordError(row.getRowNum(), i, bad + " is not valid as an ordinate.");
        }
        return null;
    }


    @Override
    public void processWorkbook(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        processSheet(sheet, 0);
    }
}
