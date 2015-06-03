package org.biopama.ibis.upload;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.biopama.edit.Dao;
import org.issg.ibis.domain.ConservationClassification;
import org.issg.ibis.domain.OrganismType;
import org.issg.ibis.domain.QConservationClassification;
import org.issg.ibis.domain.QOrganismType;
import org.issg.ibis.domain.QSpecies;
import org.issg.ibis.domain.Reference;
import org.issg.ibis.domain.Species;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeciesUploadParser2 extends UploadParser<Species> {

    private static Logger logger = LoggerFactory
            .getLogger(SpeciesUploadParser2.class);

    public SpeciesUploadParser2(Dao dao) {
        super(dao);
    }

    /**
     * {@inheritDoc}
     */
    public void processWorkbook(Workbook wb) {

        Sheet sheet = wb.getSheetAt(0);
        processSheet(sheet, 0);

    }
    
    /**
     * String cleaner which throws a horrible error if any horrible whitespace at ends that cannot be stripped is there.
     * 
     * @param toClean
     * @return
     */
    protected String cleanWhitepace(String toClean) {

        if (toClean != null) {
            toClean = toClean.replace(String.valueOf((char) 160), " ").trim();
        } else {
            return "";
        }

        if(toClean.isEmpty()) {
            return toClean;
        }
        

        char firstChar = toClean.charAt(0);
        if (! isCharAlpha(firstChar)) {
            throw new RuntimeException("Char " + firstChar + " should not be here!");
        }

        char lastChar = toClean.charAt(toClean.length()-1);

        if (! isCharAlpha(lastChar) && (lastChar != '.') &&  (lastChar != ')')) {
            throw new RuntimeException("Char " + lastChar + " should not be here!");
        }

        return toClean;
    }
    
    protected boolean isCharAlpha(char c) {
        if (c < 0x41 || (c > 0x5a && c <= 0x60) || c > 0x7a) {
            return false;
        }
        return true;
    }

    @Override
    protected Species processRow(Row row) {

        String name = getCellValueAsString(row, 1);
        name = cleanWhitepace(name);
        Species checkSpecies = dao.findByQProxyId(QSpecies.species, QSpecies.species.name, name);

//        String otLabel = getCellValueAsString(row, 9);
//		OrganismType ot = dao.findByQProxyId(QOrganismType.organismType, QOrganismType.organismType.label, otLabel);
//		
//		checkSpecies.setOrganismType(ot);

        ConservationClassification rlc = getEntity(
        		QConservationClassification.conservationClassification, 
        		QConservationClassification.conservationClassification.name, row, 5);
        checkSpecies.setConservationClassification(rlc);
//        System.out.println("Saving red list " + name + ", row" + row.getRowNum());

//        String commonName = getCellValueAsString(row, 10);

//        if (commonName != null && commonName.length() > 1) {
//        checkSpecies.setCommonName(commonName);
//        }
//
//        String refId = getCellValueAsString(row, 15);
//        
//        if (refId == null) {
//        	return null;
//        }
//        
//        Reference ref = dao.find(Reference.class, Long.valueOf(refId));
//        
//        checkSpecies.getReferences().add(ref);

        return checkSpecies;
        
    }

    /**
     * 
     * @param row
     * @param colIdx
     * @param rank
     * @return
     */
//    public Taxon getTaxon(String taxonName, TaxonomicRank taxonomicRank, Taxon parentTaxon) {
//
//
//        TypedQuery<Taxon> q = findTaxonByNameAndRank(taxonomicRank, taxonName);
//        
//        List<Taxon> res = q.getResultList();
//        
//        if (res.size() == 0) {
//            Taxon t = new Taxon();
//            t.setTaxonomicRank(taxonomicRank);
//            t.setLabel(taxonName);
//            t.setParentTaxon(parentTaxon);
//            dao.persist(t);
//            return t;
//        } else if (res.size() == 1) {
//            return res.get(0);
//        } else {
//            return null;
//        }
//    }
//
//    private TypedQuery<Taxon> findTaxonByNameAndRank(TaxonomicRank rank, String lookUp) {
//        TypedQuery<Taxon> q = dao
//                .get()
//                .createQuery(
//                        "from Taxon where upper(label) = upper(:label) and taxonomicRank = :rank",
//                        Taxon.class);
//        q.setParameter("label", lookUp);
//        q.setParameter("rank", rank);
//        return q;
//    }
}
