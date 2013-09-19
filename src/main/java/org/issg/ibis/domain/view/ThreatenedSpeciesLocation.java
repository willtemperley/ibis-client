package org.issg.ibis.domain.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Will Temperley
 *
 */
@Entity
@Table(schema = "ibis", name = "threatened_species_location")
public class ThreatenedSpeciesLocation {

    private Long id;

    @Id
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    private Long threatenedSpeciesId;

    @Column(name = "threatened_species_id")
    public Long getThreatenedSpeciesId() {
        return threatenedSpeciesId;
    }

    public void setThreatenedSpeciesId(Long threatenedSpeciesId) {
        this.threatenedSpeciesId = threatenedSpeciesId;
    }

    private String geoJson;

    @Column(name = "geojson")
    public String getGeoJson() {
        return geoJson;
    }
    
    public void setGeoJson(String geoJson) {
        this.geoJson = geoJson;
    }

}
