package org.issg.ibis.domain.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.issg.ibis.domain.Species;

@Entity
@Table(schema = "ibis", name = "species_impact_view")
public class SpeciesImpactView {

    private Long id;

    @Id
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    private Species nativeSpecies;

    @ManyToOne
    @JoinColumn(name="native_species_id")
    public Species getNativeSpecies() {
        return nativeSpecies;
    }

    public void setNativeSpecies(Species nativeSpecies) {
        this.nativeSpecies = nativeSpecies;
    }

    private String country;

    @Column
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    private String impactMechanism;

    @Column(name="impact_mechanism")
    public String getImpactMechanism() {
        return impactMechanism;
    }

    public void setImpactMechanism(String impactMechanism) {
        this.impactMechanism = impactMechanism;
    }

    private String impactOutcome;

    @Column(name="impact_outcome")
    public String getImpactOutcome() {
        return impactOutcome;
    }

    public void setImpactOutcome(String impactOutcome) {
        this.impactOutcome = impactOutcome;
    }

    private String biologicalStatus;

    @Column(name="biological_status")
    public String getBiologicalStatus() {
        return biologicalStatus;
    }

    public void setBiologicalStatus(String biologicalStatus) {
        this.biologicalStatus = biologicalStatus;
    }

    private String location;

    @Column
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String invasiveSpecies;

    @Column(name="invasive_species")
    public String getInvasiveSpecies() {
        return invasiveSpecies;
    }

    public void setInvasiveSpecies(String invasiveSpecies) {
        this.invasiveSpecies = invasiveSpecies;
    }

    private String invasiveCommonName;

    @Column(name="invasive_common_name")
    public String getInvasiveCommonName() {
        return invasiveCommonName;
    }

    public void setInvasiveCommonName(String invasiveCommonName) {
        this.invasiveCommonName = invasiveCommonName;
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

        if (obj instanceof SpeciesImpactView) {
           SpeciesImpactView otherObj = (SpeciesImpactView) obj;
           if (otherObj.getId().equals(this.getId())) {
                return true;
           }
           return false;
        }
        return super.equals(obj);
    }
}
