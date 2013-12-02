package org.issg.ibis.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import java.util.Set;

@Entity
@Table(schema = "ibis", name = "reference")
@Deprecated
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

    private String content;

    @Column
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private Set<Species> speciess;

    @ManyToMany
    @JoinTable(name = "ibis.species_reference", joinColumns = @JoinColumn(name = "reference_id"), inverseJoinColumns = @JoinColumn(name = "species_id"))
    public Set<Species> getSpeciess() {
        return speciess;
    }

    public void setSpeciess(Set<Species> speciess) {
        this.speciess = speciess;
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

        if (obj instanceof Reference) {
           Reference otherObj = (Reference) obj;
           if (otherObj.getId().equals(this.getId())) {
                return true;
            }
        }
        return super.equals(obj);
    }
}
