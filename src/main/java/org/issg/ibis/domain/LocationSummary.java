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

@Entity
@Table(schema = "ibis", name = "location_summary")
public class LocationSummary implements Content {

    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(allocationSize = 1, name = "seq", sequenceName = "ibis.location_summary_id_seq")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    private ContentType contentType;

    @NotNull
    @ManyToOne
    @JoinColumn(name="content_type_id")
    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    private String content;

    @NotNull
    @Column
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private Location location;

    @NotNull
    @ManyToOne
    @JoinColumn(name="location_id")
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int hashCode() {
        if (id != null) {
            id.intValue();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof LocationSummary) {
           LocationSummary otherObj = (LocationSummary) obj;

           Long id2 = otherObj.getId();
           if (id2 != null && id2.equals(this.getId())) {
                return true;
           }
           return false;
        }
        return super.equals(obj);
    }
}
