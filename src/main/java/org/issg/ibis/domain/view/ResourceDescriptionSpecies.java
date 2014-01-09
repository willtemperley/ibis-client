package org.issg.ibis.domain.view;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.issg.ibis.domain.Species;

@Entity
@Table(schema = "ibis", name = "resource_description_species")
public class ResourceDescriptionSpecies {

    private String id;

    @Id
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    private Species species;

    @ManyToOne
    @JoinColumn(name = "species_id")
    public Species getSpecies() {
        return species;
    }
    public void setSpecies(Species species) {
        this.species = species;
    }

    private ResourceDescription resourceDescription;

    @ManyToOne
    @JoinColumn(name = "resource_description_id")
    public ResourceDescription getResourceDescription() {
        return resourceDescription;
    }

    public void setResourceDescription(ResourceDescription resourceDescription) {
        this.resourceDescription = resourceDescription;
    }

}
