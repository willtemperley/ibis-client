package org.issg.ibis.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import org.jrc.persist.adminunits.Country;

@Entity
@Table(schema = "ibis", name = "faceted_search")
public class FacetedSearch {

    private String id;

    @Id
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    private Country country;

    @ManyToOne
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    private String designatedAreaType;

    @Column(name="designated_area_type")
    public String getDesignatedAreaType() {
        return designatedAreaType;
    }

    public void setDesignatedAreaType(String designatedAreaType) {
        this.designatedAreaType = designatedAreaType;
    }
    
    private ResourceType resourceType;

    @ManyToOne
    @JoinColumn(name = "resource_type_id")
    public ResourceType getResourceType() {
        return resourceType;
    }
    
    public void setResourceType(ResourceType resouceType) {
        this.resourceType = resouceType;
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

        if (obj instanceof FacetedSearch) {
           FacetedSearch otherObj = (FacetedSearch) obj;
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
