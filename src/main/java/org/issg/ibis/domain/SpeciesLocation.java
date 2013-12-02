package org.issg.ibis.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(schema = "ibis", name = "species_location")
public class SpeciesLocation {

    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(allocationSize = 1, name = "seq", sequenceName = "ibis.species_location_id_seq")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    private Location location;

    @NotNull
    @ManyToOne
    @JoinColumn(name="location_id")
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    private BiologicalStatus biologicalStatus;

    @NotNull
    @ManyToOne
    @JoinColumn(name="biological_status_id")
    public BiologicalStatus getBiologicalStatus() {
        return biologicalStatus;
    }

    public void setBiologicalStatus(BiologicalStatus biologicalStatus) {
        this.biologicalStatus = biologicalStatus;
    }

    private Species species;

    @NotNull
    @ManyToOne
    @JoinColumn(name="species_id")
    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    @Override
    public int hashCode() {
        if (id != null) {
            id.intValue();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof SpeciesLocation) {
           SpeciesLocation otherObj = (SpeciesLocation) obj;
           if (otherObj.getId().equals(this.getId())) {
                return true;
           }
           return false;
        }
        return super.equals(obj);
    }
}
