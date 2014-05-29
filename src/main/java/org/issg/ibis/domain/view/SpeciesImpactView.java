package org.issg.ibis.domain.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

    private String nativeSpecies;

    @Column(name="native_species")
    public String getNativeSpecies() {
        return nativeSpecies;
    }

    public void setNativeSpecies(String nativeSpecies) {
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
