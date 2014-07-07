package org.issg.ibis.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "ibis", name = "location_type")
public class LocationType {

    private String id;

    @Id
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
    	return getId();
    }

}
