package org.issg.ibis.domain.view;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;

@Entity
@Table(schema = "ibis", name = "rd")
public class ResourceDescription {

    private String id;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    private String name;

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Integer impactCount;
    
    @Column(name = "impact_count")
    public Integer getImpactCount() {
        return impactCount;
    }

    public void setImpactCount(Integer impactCount) {
        this.impactCount = impactCount;
    }
    
    private String resultType;
    
    @Column(name = "result_type")
    public String getResultType() {
		return resultType;
	}
    
    public void setResultType(String resultType) {
		this.resultType = resultType;
	}
    
    private Set<Species> species;

    @ManyToMany
    @JoinTable(name = "ibis.rds", joinColumns = @JoinColumn(name = "resource_description_id"), inverseJoinColumns = @JoinColumn(name = "species_id"))
    public Set<Species> getSpecies() {
        return species;
    }

    public void setSpecies(Set<Species> species) {
        this.species = species;
    }

    private Set<Location> locations;

    @ManyToMany
    @JoinTable(name = "ibis.rdl", joinColumns = @JoinColumn(name = "resource_description_id"), inverseJoinColumns = @JoinColumn(name = "location_id"))
    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
		this.locations = locations;
	}

    @Override
    public int hashCode() {
        if (id != null) {
            id.hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof ResourceDescription) {
           ResourceDescription otherObj = (ResourceDescription) obj;
           if (otherObj.getId().equals(this.getId())) {
                return true;
           }
           return false;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return name;
    }
}
