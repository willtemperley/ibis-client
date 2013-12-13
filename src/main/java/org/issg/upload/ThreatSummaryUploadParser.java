package org.issg.upload;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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

        Sheet sheet = wb.getSheetAt(3);
        processSheet(sheet, 0);

    }

    @Override
    protected Content processRow(Row row) {

        String ns = getCellValueAsString(row, 0);
        String identifier = getCellValueAsString(row, 1);

        EntityManager em = dao.getEntityManager();

        if (ns.equals("REDLIST") || ns.equals("RED_LIST")) {
            Long objId = getCellValueAsLong(row, 1);

            TypedQuery<Species> q = em.createQuery("from Species where redlistId = :rl_id", Species.class);
            q.setParameter("rl_id", objId.intValue());
            Species species = q.getSingleResult();

            System.out.println("processing species " + species);

            SpeciesSummary speciesSummary = new SpeciesSummary();
            speciesSummary.setSpecies(species);
            getContent(row, speciesSummary);

            return speciesSummary;
        }

        
        {
            String locFullId = ns + ":" + identifier;
            System.out.println(locFullId);
            TypedQuery<Location> l = em.createQuery("from Location where prefix = :prefix and identifier = :identifier", Location.class);
            l.setParameter("prefix", ns);
            l.setParameter("identifier", identifier);
            
            LocationSummary locationSummary = new LocationSummary();
            
            Location location = l.getSingleResult();
            locationSummary.setLocation(location);
            
            if (location == null) {
                recordError(row.getRowNum(), 1, "Could not find location: " + locFullId);
            }

            getContent(row, locationSummary);
            
            return locationSummary;
        }
        

    }

    private void getContent(Row row, Content content) {
        content.setContent(getCellValueAsString(row, 2));
        content.setContentType(getEntity(ContentType_.name, row, 3));
    }
}
