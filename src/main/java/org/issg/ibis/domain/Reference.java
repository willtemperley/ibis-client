package org.issg.ibis.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema = "ibis", name = "reference")
public class Reference {

    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(allocationSize = 1, name = "seq", sequenceName = "ibis.reference_id_seq")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    private String referenceHeader;

    @Column(name="reference_header")
    public String getReferenceHeader() {
        return referenceHeader;
    }

    public void setReferenceHeader(String referenceHeader) {
        this.referenceHeader = referenceHeader;
    }

    private String referenceContext;

    @Column(name="reference_context")
    public String getReferenceContext() {
        return referenceContext;
    }

    public void setReferenceContext(String referenceContext) {
        this.referenceContext = referenceContext;
    }

    private String referenceFull;

    @Column(name="reference_full")
    public String getReferenceFull() {
        return referenceFull;
    }

    public void setReferenceFull(String referenceFull) {
        this.referenceFull = referenceFull;
    }

    private String referenceCode;

    @Column(name="reference_code")
    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    private Set<Species> speciess;

    @ManyToMany
    @JoinTable(name = "ibis.reference_link", joinColumns = @JoinColumn(name = "reference_id"), inverseJoinColumns = @JoinColumn(name = "species_id"))
    public Set<Species> getSpeciess() {
        return speciess;
    }

    public void setSpeciess(Set<Species> speciess) {
        this.speciess = speciess;
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Reference) {
           Reference otherObj = (Reference) obj;
           if (otherObj.getId().equals(this.getId())) {
                return true;
            }
        }
        return super.equals(obj);
    }
    
    @Override
    public String toString() {
        return referenceHeader;
    }
}
