package org.issg.ibis.domain.view;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import org.issg.ibis.domain.ResourceType;
import org.issg.ibis.domain.Species;
import org.jrc.persist.adminunits.Country;

@Entity
@Table(schema = "ibis", name = "resource_description")
public class ResourceDescription {

    private String id;

    @Id
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    private Set<Species> invasiveSpecies;

    @ManyToMany
    @JoinTable(name = "ibis.search_entity", joinColumns = @JoinColumn(name = "search_entity_id"), inverseJoinColumns = @JoinColumn(name = "species_id"))
    public Set<Species> getInvasiveSpecies() {
        return invasiveSpecies;
    }

    public void setInvasiveSpecies(Set<Species> invasiveSpecies) {
        this.invasiveSpecies = invasiveSpecies;
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
