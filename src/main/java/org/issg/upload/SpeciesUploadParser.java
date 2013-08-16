package org.issg.upload;

import java.util.HashSet;

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
import org.jrc.persist.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeciesUploadParser extends UploadParser<Species> {

    private static Logger logger = LoggerFactory.getLogger(SpeciesUploadParser.class);

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

        {
            // Genus
            Genus genus = getEntity(Genus_.label, row, 2);
            species.setGenus(genus);
        }

        {
            // Species (not bionomial)
            String sp = getCellValueAsString(row, 3);
            species.setSpecies(sp);
        }
        
        {
            // Species authority
            String val = getCellValueAsString(row, 4);
            species.setAuthority(val);
        }
        
        {
            // Redlist category
            RedlistCategory rlc = getEntity(RedlistCategory_.label, row, 5);
            species.setRedlistCategory(rlc);
        }
        
        {
            // Organism type
            OrganismType ot = getEntity(OrganismType_.label, row, 6);
            species.setOrganismType(ot);
        }
        
        {
            // Biomes
            String val = getCellValueAsString(row, 7);
            if (val != null) {
                
                String[] biomes = val.split(",");
                HashSet<Biome> biomeSet = new HashSet<Biome>();
                species.setBiomes(biomeSet);
                
                for (String biomeName : biomes) {
                    Biome biome = getEntity(Biome_.label, row, 7, biomeName);
                    biomeSet.add(biome);
                }
            }
        }
        
        {
            // Common name
            String val = getCellValueAsString(row, 8);
            species.setCommonName(val);
        }

        
        return species;
    }
}
