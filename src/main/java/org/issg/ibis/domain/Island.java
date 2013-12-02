package org.issg.ibis.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
@Table(schema = "ibis", name = "island")
public class Island {

    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(allocationSize = 1, name = "seq", sequenceName = "ibis.island_id_seq")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    private Long countryId;

    @Column(name="country_id")
    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    private IslandType islandType;

    @ManyToOne
    @JoinColumn(name="island_type_id")
    public IslandType getIslandType() {
        return islandType;
    }

    public void setIslandType(IslandType islandType) {
        this.islandType = islandType;
    }

    private String islandName;

    @Column(name="island_name")
    public String getIslandName() {
        return islandName;
    }

    public void setIslandName(String islandName) {
        this.islandName = islandName;
    }

    private String islandGroup;

    @Column(name="island_group")
    public String getIslandGroup() {
        return islandGroup;
    }

    public void setIslandGroup(String islandGroup) {
        this.islandGroup = islandGroup;
    }

    private Double latitudeDec;

    @Column(name="latitude_dec")
    public Double getLatitudeDec() {
        return latitudeDec;
    }

    public void setLatitudeDec(Double latitudeDec) {
        this.latitudeDec = latitudeDec;
    }

    private Double longitudeDec;

    @Column(name="longitude_dec")
    public Double getLongitudeDec() {
        return longitudeDec;
    }

    public void setLongitudeDec(Double longitudeDec) {
        this.longitudeDec = longitudeDec;
    }

    private String latitudeDeg;

    @Column(name="latitude_deg")
    public String getLatitudeDeg() {
        return latitudeDeg;
    }

    public void setLatitudeDeg(String latitudeDeg) {
        this.latitudeDeg = latitudeDeg;
    }

    private String longitudeDeg;

    @Column(name="longitude_deg")
    public String getLongitudeDeg() {
        return longitudeDeg;
    }

    public void setLongitudeDeg(String longitudeDeg) {
        this.longitudeDeg = longitudeDeg;
    }

    private String islandNotes;

    @Column(name="island_notes")
    public String getIslandNotes() {
        return islandNotes;
    }

    public void setIslandNotes(String islandNotes) {
        this.islandNotes = islandNotes;
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.intValue();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Island) {
           Island otherObj = (Island) obj;
           if (otherObj.getId().equals(this.getId())) {
                return true;
           }
           return false;
        }
        return super.equals(obj);
    }
}
