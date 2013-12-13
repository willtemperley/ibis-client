package org.issg.upload;

import java.util.HashSet;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Location_;
import org.issg.ibis.domain.Reference;
import org.issg.ibis.domain.Reference_;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.Species_;
import org.jrc.persist.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReferenceUploadParser extends UploadParser<Reference> {

    private static Logger logger = LoggerFactory
            .getLogger(ReferenceUploadParser.class);

    public ReferenceUploadParser(Dao dao) {
        super(dao, Reference.class);
    }

    /**
     * {@inheritDoc}
     */
    public void processWorkbook(Workbook wb) {

        Sheet sheet = wb.getSheetAt(4);
        processSheet(sheet, 0);

    }

    @Override
    protected Reference processRow(Row row) {

        String lookup = getCellValueAsString(row, 0);
        String citation = getCellValueAsString(row, 1);

        Reference ref = dao.findByProxyId(Reference_.content, citation);
        if (ref == null) {
            
            for (Reference r : entityList) {
                if (r.getContent().equals(citation)) {
                    ref = r;
                }
            }

        } else {
            System.out.println("Found ref.");
        }
        
        if (ref == null) {
            ref = new Reference();
        }
        
        
        Location loc = dao.findByProxyId(Location_.name, getCellValueAsString(row, 0));

        if (loc == null){
            Species sp = dao.findByProxyId(Species_.name, lookup);

            if (sp == null) {
                recordError("Missing either loc or species: " + lookup);
            }
            Set<Species> spp = ref.getSpecies();
            if (spp == null) {
                spp = new HashSet<Species>();
                spp.add(sp);
                ref.setSpecies(spp);
                ref.setContent(citation);
            }
        } else {
            loc = getEntity(Location_.name, row, 0);
            Set<Location> spp = ref.getLocations();
            if (spp == null) {
                spp = new HashSet<Location>();
                spp.add(loc);
                ref.setLocations(spp);
                ref.setContent(citation);
            }
            if (loc == null) {
                recordError("Should have found either a loc or sp. with lookup: " + lookup);
            }
        }

        return ref;
        // ref.setSpecies(spp);
        // ref.setContent(citation);
    }

}
