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
@Table(schema = "ibis", name = "species_impact")
public class SpeciesImpact {

    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(allocationSize = 1, name = "seq", sequenceName = "ibis.species_impact_id_seq")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    private Species nativeSpecies;

    @NotNull
    @ManyToOne
    @JoinColumn(name="threatened_species_id")
    public Species getNativeSpecies() {
        return nativeSpecies;
    }

    public void setNativeSpecies(Species nativeSpecies) {
        this.nativeSpecies = nativeSpecies;
    }

    private Species invasiveSpecies;

    @NotNull
    @ManyToOne
    @JoinColumn(name="invasive_species_id")
    public Species getInvasiveSpecies() {
        return invasiveSpecies;
    }

    public void setInvasiveSpecies(Species invasiveSpecies) {
        this.invasiveSpecies = invasiveSpecies;
    }

    private ImpactMechanism impactMechanism;

    @NotNull
    @ManyToOne
    @JoinColumn(name="impact_mechanism_id")
    public ImpactMechanism getImpactMechanism() {
        return impactMechanism;
    }

    public void setImpactMechanism(ImpactMechanism impactMechanism) {
        this.impactMechanism = impactMechanism;
    }

    private ImpactOutcome impactOutcome;

    @NotNull
    @ManyToOne
    @JoinColumn(name="impact_outcome_id")
    public ImpactOutcome getImpactOutcome() {
        return impactOutcome;
    }

    public void setImpactOutcome(ImpactOutcome impactOutcome) {
        this.impactOutcome = impactOutcome;
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

//    @NotNull
    @ManyToOne
    @JoinColumn(name="biological_status_id")
    public BiologicalStatus getBiologicalStatus() {
        return biologicalStatus;
    }

    public void setBiologicalStatus(BiologicalStatus biologicalStatus) {
        this.biologicalStatus = biologicalStatus;
    }

    private Reference reference;

    @ManyToOne
    @JoinColumn(name="reference_id")
    public Reference getReference() {
		return reference;
	}

    public void setReference(Reference reference) {
		this.reference = reference;
	}


    @Override
    public int hashCode() {
        if (id != null) {
            return id.intValue();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof SpeciesImpact) {
           SpeciesImpact otherObj = (SpeciesImpact) obj;
           if (otherObj.getId().equals(this.getId())) {
                return true;
           }
           return false;
        }
        return super.equals(obj);
    }
}
