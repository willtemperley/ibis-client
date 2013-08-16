package org.issg.ibis.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

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

    private Species threatenedSpecies;

    @NotNull
    @ManyToOne
    @JoinColumn(name="threatened_species_id")
    public Species getThreatenedSpecies() {
        return threatenedSpecies;
    }

    public void setThreatenedSpecies(Species threatenedSpecies) {
        this.threatenedSpecies = threatenedSpecies;
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

    @ManyToOne
    @JoinColumn(name="location_id")
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    
    private String toString;
    
    @Override
    public String toString() {
        if (toString == null) {
            toString = String.format("%s - %s", threatenedSpecies, invasiveSpecies);
        }
        return toString;
    }
}
