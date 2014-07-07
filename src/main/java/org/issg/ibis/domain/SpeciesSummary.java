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
@Table(schema = "ibis", name = "species_summary")
public class SpeciesSummary implements Content {

    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(allocationSize = 1, name = "seq", sequenceName = "ibis.species_summary_id_seq")
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    private ContentType contentType;

    /* (non-Javadoc)
     * @see org.issg.ibis.domain.Content#getContentType()
     */
    @Override
    @NotNull
    @ManyToOne
    @JoinColumn(name="content_type_id")
    public ContentType getContentType() {
        return contentType;
    }

    /* (non-Javadoc)
     * @see org.issg.ibis.domain.Content#setContentType(org.issg.ibis.domain.ContentType)
     */
    @Override
    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    private String content;

    /* (non-Javadoc)
     * @see org.issg.ibis.domain.Content#getContent()
     */
    @Override
    @NotNull
    @Column
    public String getContent() {
        return content;
    }

    /* (non-Javadoc)
     * @see org.issg.ibis.domain.Content#setContent(java.lang.String)
     */
    public void setContent(String content) {
        this.content = content;
    }

    private Species species;

    @NotNull
    @ManyToOne
    @JoinColumn(name="species_id")
    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
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
        
        if (obj instanceof SpeciesSummary) {
           SpeciesSummary otherObj = (SpeciesSummary) obj;
           if (otherObj.getId() == null) {
               return false;
           }
           if (otherObj.getId().equals(this.getId())) {
                return true;
           }
           return false;
        }
        return super.equals(obj);
    }
}
