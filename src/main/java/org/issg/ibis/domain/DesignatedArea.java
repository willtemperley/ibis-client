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
@Table(schema = "ibis", name = "designated_area")
public class DesignatedArea {

    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(allocationSize = 1, name = "seq", sequenceName = "ibis.designated_area_id_seq")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    private String areaid;

    @Column
    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    private Long idcountry;

    @Column
    public Long getIdcountry() {
        return idcountry;
    }

    public void setIdcountry(Long idcountry) {
        this.idcountry = idcountry;
    }

    private Island island;

    @ManyToOne
    @JoinColumn(name="island_id")
    public Island getIsland() {
        return island;
    }

    public void setIsland(Island island) {
        this.island = island;
    }

    private String siteName;

    @Column(name="site_name")
    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    private DesignatedAreaType designatedAreaType;

    @ManyToOne
    @JoinColumn(name="designated_area_type_id")
    public DesignatedAreaType getDesignatedAreaType() {
        return designatedAreaType;
    }

    public void setDesignatedAreaType(DesignatedAreaType designatedAreaType) {
        this.designatedAreaType = designatedAreaType;
    }

    private Double totalsize;

    @Column
    public Double getTotalsize() {
        return totalsize;
    }

    public void setTotalsize(Double totalsize) {
        this.totalsize = totalsize;
    }

    private Long latitude;

    @Column
    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    private Long longitude;

    @Column
    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    private String url;

    @Column
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String comments;

    @Column
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

        if (obj instanceof DesignatedArea) {
           DesignatedArea otherObj = (DesignatedArea) obj;
           if (otherObj.getId().equals(this.getId())) {
                return true;
           }
           return false;
        }
        return super.equals(obj);
    }
}
