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
@Table(schema = "ibis", name = "taxon")
public class Taxon {

    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(allocationSize = 1, name = "seq", sequenceName = "ibis.taxon_id_seq")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    private String label;

    @NotNull
    @Column
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    private TaxonomicRank taxonomicRank;

    @NotNull
    @ManyToOne
    @JoinColumn(name="taxonomic_rank_id")
    public TaxonomicRank getTaxonomicRank() {
        return taxonomicRank;
    }

    public void setTaxonomicRank(TaxonomicRank taxonomicRank) {
        this.taxonomicRank = taxonomicRank;
    }

    private Taxon parentTaxon;

    @ManyToOne
    @JoinColumn(name="parent_taxon_id")
    public Taxon getParentTaxon() {
        return parentTaxon;
    }

    public void setParentTaxon(Taxon parentTaxon) {
        this.parentTaxon = parentTaxon;
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

        if (obj instanceof Taxon) {
           Taxon otherObj = (Taxon) obj;
           if (otherObj.getId().equals(this.getId())) {
                return true;
           }
           return false;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return label;
    }
}
