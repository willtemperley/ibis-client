package org.issg.ibis.domain;

import javax.persistence.Column;
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
@Table(schema = "administrative_units", name = "country")
public class Country {

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

    private String isoa3Id;

    @NotNull
    @Column(name="isoa3_id")
    public String getIsoa3Id() {
        return isoa3Id;
    }

    public void setIsoa3Id(String isoa3Id) {
        this.isoa3Id = isoa3Id;
    }

    private Region region;
    
    @ManyToOne
    @JoinColumn(name = "region_id")
    public Region getRegion() {
		return region;
	}
    
    public void setRegion(Region region) {
		this.region = region;
	}

//    private Polygon envelope;
//
//    @Column
//    @Type(type = "org.hibernate.spatial.GeometryType")
//    public Polygon getEnvelope() {
//        return envelope;
//    }
//
//    public void setEnvelope(Polygon envelope) {
//        this.envelope = envelope;
//    }

    private String name;

    @NotNull
    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    private Polygon env;
//
//    @Column
//    public Polygon getEnv() {
//        return env;
//    }
//
//    public void setEnv(Polygon env) {
//        this.env = env;
//    }
//
//    private Polygon geom;
//
//    @Column
//    public Polygon getGeom() {
//        return geom;
//    }
//
//    public void setGeom(Polygon geom) {
//        this.geom = geom;
//    }

    private String isoa2Id;

    @NotNull
    @Column(name="isoa2_id")
    public String getIsoa2Id() {
        return isoa2Id;
    }

    public void setIsoa2Id(String isoa2Id) {
        this.isoa2Id = isoa2Id;
    }

    
    @Override
    public boolean equals(Object obj) {
        
        if (obj instanceof Country) {
            Country comparee = (Country) obj;
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
