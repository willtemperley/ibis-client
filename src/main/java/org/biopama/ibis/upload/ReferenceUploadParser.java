package org.biopama.ibis.upload;

import java.util.HashSet;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.biopama.edit.Dao;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.QLocation;
import org.issg.ibis.domain.QReference;
import org.issg.ibis.domain.QSpecies;
import org.issg.ibis.domain.Reference;
import org.issg.ibis.domain.Species;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReferenceUploadParser extends UploadParser<Reference> {

    private static Logger logger = LoggerFactory
            .getLogger(ReferenceUploadParser.class);

    public ReferenceUploadParser(Dao dao) {
        super(dao);
    }

    /**
     * {@inheritDoc}
     */
    public void processWorkbook(Workbook wb) {

        Sheet sheet = wb.getSheetAt(0);
        processSheet(sheet, 0);

    }

    @Override
    protected Reference processRow(Row row) {
    	
    	Long refId = getCellValueAsLong(row, 0);
    	
    	Reference ref = dao.find(Reference.class, refId);
    	
    	if (ref == null) {
    		ref = new Reference();
    		ref.setId(refId);
    	}

    	ref.setLabel(getCellValueAsString(row, 1));
        ref.setUrl(getCellValueAsString(row, 2));
        String url = ref.getUrl();
        if (url!= null) {
        	ref.setContent(String.format("<a href='%s'>%s</a>", url, ref.getLabel()));
        } else {
        	ref.setContent(ref.getLabel());
        }

        return ref;


//        String lookup = getCellValueAsString(row, 0);
//        String citation = getCellValueAsString(row, 1);
//
//        Reference ref = dao.findByQProxyId(QReference.reference, QReference.reference.content, citation);
//        if (ref == null) {
//            
//            for (Reference r : entityList) {
//                if (r.getContent().equals(citation)) {
//                    ref = r;
//                }
//            }
//
//        } else {
//            System.out.println("Found ref.");
//        }
//        
//        if (ref == null) {
//            ref = new Reference();
//        }
//        
//        
//        Location loc = dao.findByQProxyId(QLocation.location, QLocation.location.name, getCellValueAsString(row, 0));
//
//        if (loc == null){
//            Species sp = dao.findByQProxyId(QSpecies.species, QSpecies.species.name, lookup);
//
//            if (sp == null) {
//                recordError("Missing either loc or species: " + lookup);
//            }
//            Set<Species> spp = ref.getSpecies();
//            if (spp == null) {
//                spp = new HashSet<Species>();
//                spp.add(sp);
//                ref.setSpecies(spp);
//                ref.setContent(citation);
//            }
//        } else {
//            loc = getEntity(QLocation.location, QLocation.location.name, row, 0);
//            Set<Location> spp = ref.getLocations();
//            if (spp == null) {
//                spp = new HashSet<Location>();
//                spp.add(loc);
//                ref.setLocations(spp);
//                ref.setContent(citation);
//            }
//            if (loc == null) {
//                recordError("Should have found either a loc or sp. with lookup: " + lookup);
//            }
//        }

        // ref.setSpecies(spp);
        // ref.setContent(citation);
    }

}
