package org.issg.upload;

import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.TypedQuery;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.issg.ibis.domain.Biome;
import org.issg.ibis.domain.Biome_;
import org.issg.ibis.domain.OrganismType;
import org.issg.ibis.domain.OrganismType_;
import org.issg.ibis.domain.RedlistCategory;
import org.issg.ibis.domain.RedlistCategory_;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.Species_;
import org.issg.ibis.domain.Taxon;
import org.issg.ibis.domain.TaxonomicRank;
import org.issg.ibis.domain.TaxonomicRank_;
import org.issg.ibis.webservices.GbifApi09;
import org.jrc.edit.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TempSpeciesUploadParser extends UploadParser<Species> {

    private static Logger logger = LoggerFactory
            .getLogger(TempSpeciesUploadParser.class);

    public TempSpeciesUploadParser(Dao dao) {
        super(dao, Species.class);
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

//        if (row.getRowNum() >= 1500) {
//            return null;
//        }
        Species species = new Species();
        
        {
            // Name
            String name = getCellValueAsString(row, 9);
            name = cleanWhitepace(name);
            species.setName(name);
            System.out.println("Processing: " + name + ", row" + row.getRowNum());
            
            // Check species 
            Species checkSpecies = dao.findByProxyId(Species_.name, name);
            if (checkSpecies != null) {
                
                //Re-try redlist
                if (checkSpecies.getRedlistCategory() == null) {
                    RedlistCategory rlc = getEntity(RedlistCategory_.label, row, 12);
                    checkSpecies.setRedlistCategory(rlc);
                    System.out.println("Saving red list " + name + ", row" + row.getRowNum());
                    return checkSpecies;
                }
                if (checkSpecies.getOrganismType() == null) {
                    OrganismType ot = getEntity(OrganismType_.label, row, 13);
                    checkSpecies.setOrganismType(ot);
                    return checkSpecies;
                }
                
                logger.info("Skipping species which already exists in DB: " + name);
                return null;
//                recordError(row.getRowNum(), 1, String.format("Species %s already exists", checkSpecies.getName()));
            }
            
            // Avoid duplicate species in upload
            List<Species> entities = getEntityList();
            for (Species alreadyProcessed : entities) {
                if (species.getName().equals(alreadyProcessed.getName())) {
                    recordError(row.getRowNum(), 1, String.format("Duplicate %s species in upload", species.getName()));
                }
            }
        }

        /*
         * Look up species by RL or GBIF uri
         */
        {
            String prefix = getCellValueAsString(row, 0);
            if (prefix.equals("REDLIST")) {

                Long id = getCellValueAsLong(row, 1);
                if (id != null) {
                    species.setRedlistId(id.intValue());
                    GbifApi09.populateSpeciesFromRedlistId(species);
                }
                
                if (species.getUri() == null) {
                    logger.info("Lookup failed for species on RL ID: " + species.getRedlistId());
                    return null;
                }

            } else if (prefix.equals("GBIF")) {

                String gbifUri = getCellValueAsString(row, 1);
                species.setUri(gbifUri);
                
                GbifApi09.populateSpeciesFromGbifUri(species);
                
            } else {
                recordError("Unknown prefix: " + prefix);
            }
            
        }
        
        {
            // Organism type
            OrganismType ot = getEntity(OrganismType_.label, row, 13);
            species.setOrganismType(ot);
        }

        {
            // Biomes
            String val = getCellValueAsString(row, 14);
            if (val != null && !val.isEmpty()) {

                String[] biomes = val.split("/");
                HashSet<Biome> biomeSet = new HashSet<Biome>();
                species.setBiomes(biomeSet);

                for (String biomeName : biomes) {
                    Biome biome = getEntity(Biome_.label, row, 12, biomeName);
                    biomeSet.add(biome);
                }
            }
        }

        {
            // Common name
            String val = getCellValueAsString(row, 11);
            species.setCommonName(val);
        }

        return species;

//        TaxonomicRank rank = dao.findByProxyId(TaxonomicRank_.label, "Kingdom");
//        String kingdomName = getCellValueAsString(row, 2);
//        Taxon parentTaxon = this.findTaxonByNameAndRank(rank, kingdomName).getSingleResult();
//                
//        //i ranges from phylum (5) to genus(9)
//        for (int i = 5; i < 10; i++) {
//            
//            String taxonName = getCellValueAsString(row, i);
//            
//            TaxonomicRank taxonomicRank = dao.find(TaxonomicRank.class, Long.valueOf(i-1));
//            Taxon taxon = getTaxon(taxonName, taxonomicRank, parentTaxon);
//            if (taxon == null) {
//                recordError(row.getRowNum(), i, "Multiple taxa found for lookup \" "+ taxonName + "\"" );
//            }
//            
//            parentTaxon = taxon;
//            if (i == 7) {
//                species.setGenus(taxon);
//            }
//        }

//        {
//            // Species (not bionomial)
//            String sp = getCellValueAsString(row, 8);
//            species.setSpecies(sp);
//        }

//        {
//            // Species authority
//            String val = getCellValueAsString(row, 12);
//            species.setAuthority(val);
//        }

//        {
//            // Redlist category
//            RedlistCategory rlc = getEntity(RedlistCategory_.label, row, 14);
//            species.setRedlistCategory(rlc);
//        }
    }

    /**
     * 
     * @param row
     * @param colIdx
     * @param rank
     * @return
     */
    public Taxon getTaxon(String taxonName, TaxonomicRank taxonomicRank, Taxon parentTaxon) {


        TypedQuery<Taxon> q = findTaxonByNameAndRank(taxonomicRank, taxonName);
        
        List<Taxon> res = q.getResultList();
        
        if (res.size() == 0) {
            Taxon t = new Taxon();
            t.setTaxonomicRank(taxonomicRank);
            t.setLabel(taxonName);
            t.setParentTaxon(parentTaxon);
            dao.persist(t);
            return t;
        } else if (res.size() == 1) {
            return res.get(0);
        } else {
            return null;
        }
    }

    private TypedQuery<Taxon> findTaxonByNameAndRank(TaxonomicRank rank, String lookUp) {
        TypedQuery<Taxon> q = dao
                .getEntityManager()
                .createQuery(
                        "from Taxon where upper(label) = upper(:label) and taxonomicRank = :rank",
                        Taxon.class);
        q.setParameter("label", lookUp);
        q.setParameter("rank", rank);
        return q;
    }
}
