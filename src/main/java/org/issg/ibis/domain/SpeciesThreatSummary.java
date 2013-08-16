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
@Table(schema = "ibis", name = "species_threat_summary")
public class SpeciesThreatSummary {

    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(allocationSize = 1, name = "seq", sequenceName = "ibis.species_threat_summary_id_seq")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    private Species species;

    @ManyToOne
    @JoinColumn(name="species_id")
    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    private Long idcountry;

    @Column
    public Long getIdcountry() {
        return idcountry;
    }

    public void setIdcountry(Long idcountry) {
        this.idcountry = idcountry;
    }

    private String threatsummarytitle;

    @Column
    public String getThreatsummarytitle() {
        return threatsummarytitle;
    }

    public void setThreatsummarytitle(String threatsummarytitle) {
        this.threatsummarytitle = threatsummarytitle;
    }

    private String threatsummaryshort;

    @Column
    public String getThreatsummaryshort() {
        return threatsummaryshort;
    }

    public void setThreatsummaryshort(String threatsummaryshort) {
        this.threatsummaryshort = threatsummaryshort;
    }

    private String threatSummary;

    @Column(name="threat_summary")
    public String getThreatSummary() {
        return threatSummary;
    }

    public void setThreatSummary(String threatSummary) {
        this.threatSummary = threatSummary;
    }

    private String speciesManagement;

    @Column(name="species_management")
    public String getSpeciesManagement() {
        return speciesManagement;
    }

    public void setSpeciesManagement(String speciesManagement) {
        this.speciesManagement = speciesManagement;
    }

    private String conservationOutcomes;

    @Column(name="conservation_outcomes")
    public String getConservationOutcomes() {
        return conservationOutcomes;
    }

    public void setConservationOutcomes(String conservationOutcomes) {
        this.conservationOutcomes = conservationOutcomes;
    }

    private String threatsummarycode;

    @Column
    public String getThreatsummarycode() {
        return threatsummarycode;
    }

    public void setThreatsummarycode(String threatsummarycode) {
        this.threatsummarycode = threatsummarycode;
    }
}
