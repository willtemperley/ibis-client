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

import org.hibernate.annotations.Type;
import org.jrc.persist.adminunits.Country;

import com.vividsolutions.jts.geom.Polygon;

@Entity
@Table(schema = "ibis", name = "location")
public class Location {

    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(allocationSize = 1, name = "seq", sequenceName = "ibis.location_id_seq")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    private String locationId;

    @NotNull
    @Column(name="location_id")
    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
    
    private Polygon envelope;

    @Column
    @Type(type = "org.hibernate.spatial.GeometryType")
    public Polygon getEnvelope() {
        return envelope;
    }

    public void setEnvelope(Polygon envelope) {
        this.envelope = envelope;
    }

    private String name;

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String localName;

    @Column(name="local_name")
    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    private Country country;

    @ManyToOne
    @JoinColumn(name="country_id")
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
