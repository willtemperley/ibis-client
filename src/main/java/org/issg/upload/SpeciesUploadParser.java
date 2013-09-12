package org.issg.upload;

import java.util.HashSet;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.issg.ibis.domain.Biome;
import org.issg.ibis.domain.Biome_;
import org.issg.ibis.domain.Genus;
import org.issg.ibis.domain.Genus_;
import org.issg.ibis.domain.OrganismType;
import org.issg.ibis.domain.OrganismType_;
import org.issg.ibis.domain.RedlistCategory;
import org.issg.ibis.domain.RedlistCategory_;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.Taxon;
import org.issg.ibis.domain.Taxon_;
import org.issg.ibis.domain.TaxonomicRank;
import org.issg.ibis.domain.TaxonomicRank_;
import org.jrc.persist.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeciesUploadParser extends UploadParser<Species> {

    private static Logger logger = LoggerFactory
            .getLogger(SpeciesUploadParser.class);

    public SpeciesUploadParser(Dao dao) {
        super(dao, Species.class);
    }

    /**
     * {@inheritDoc}
     */
    public void processWorkbook(Workbook wb) {

        Sheet sheet = wb.getSheetAt(0);
        processSheet(sheet, 0);

    }

    @Override
    protected Species processRow(Row row) {

        Species species = new Species();

        {
            // Redlist id
            Long id = getCellValueAsLong(row, 0);
            species.setRedlistId(id);
        }

        {
            // Name
            String name = getCellValueAsString(row, 1);
            species.setName(name);
        }

        TaxonomicRank rank = dao.findByProxyId(TaxonomicRank_.label, "Kingdom");
        String kingdomName = getCellValueAsString(row, 2);
        Taxon parentTaxon = this.findTaxonByNameAndRank(rank, kingdomName).getSingleResult();
                
        //i ranges from phylum (3) to genus(7)
        for (int i = 3; i < 8; i++) {
            
            String taxonName = getCellValueAsString(row, i);
            
            TaxonomicRank taxonomicRank = dao.find(TaxonomicRank.class, Long.valueOf(i-1));
            Taxon taxon = getTaxon(taxonName, taxonomicRank, parentTaxon);
            if (taxon == null) {
                recordError(row.getRowNum(), i, "Multiple taxa found for lookup \" "+ taxonName + "\"" );
            }
            
            parentTaxon = taxon;
            if (i == 7) {
                species.setGenus(taxon);
            }
        }
        
        

        {
            // Species (not bionomial)
            String sp = getCellValueAsString(row, 8);
            species.setSpecies(sp);
        }

        {
            // Species authority
            String val = getCellValueAsString(row, 9);
            species.setAuthority(val);
        }

        {
            // Redlist category
            RedlistCategory rlc = getEntity(RedlistCategory_.label, row, 10);
            species.setRedlistCategory(rlc);
        }

        {
            // Organism type
            OrganismType ot = getEntity(OrganismType_.label, row, 11);
            species.setOrganismType(ot);
        }

        {
            // Biomes
            String val = getCellValueAsString(row, 12);
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
            String val = getCellValueAsString(row, 13);
            species.setCommonName(val);
        }

        return species;
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
