package org.issg.ibis.domain.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Polygon;

@Entity
@Table(schema = "ibis", name = "species_extent")
/**
 * 
 * @author Will Temperley
 *
 */
public class SpeciesExtent {

    private Long id;

    @Id
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof SpeciesExtent) {
           SpeciesExtent otherObj = (SpeciesExtent) obj;
           if (otherObj.getId().equals(this.getId())) {
                return true;
            }
        }
        return super.equals(obj);
    }

}
