package org.issg.ibis.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(schema = "administrative_units", name = "region")
public class Region {

    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(allocationSize = 1, name = "seq", sequenceName = "")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
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
    
    private Set<Country> countries;
    
    @OneToMany(mappedBy = "region")
    public Set<Country> getCountries() {
		return countries;
	}
    
    public void setCountries(Set<Country> countries) {
		this.countries = countries;
	}
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj instanceof Region) {
            Region comparee = (Region) obj;
            if (comparee.getId().equals(getId())) {
                return true;
            }
        }
        return super.equals(obj);
    }
    
    @Override
    public int hashCode() {
        return id.intValue();
    }
    
    @Override
    public String toString() {
        return name;
    }

}
