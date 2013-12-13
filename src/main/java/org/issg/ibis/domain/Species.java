package org.issg.ibis.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
        @NamedQuery(name = Species.THREATENED, query = "from Species s where exists (select 1 from SpeciesImpact i where i.threatenedSpecies.id = s.id) order by name"),
        @NamedQuery(name = Species.INVASIVE, query = "from Species s where exists (select 1 from SpeciesImpact i where i.invasiveSpecies.id = s.id) order by name") }
)
@Table(schema = "ibis", name = "species")
public class Species {
    
    public static final String THREATENED = "Threatened";

    public static final String INVASIVE = "Invasive";

    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(allocationSize = 1, name = "seq", sequenceName = "ibis.species_id_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String uri;

    @Column
    @NotNull
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    private String name;

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Long nubKey;

    @Column(name = "nub_key")
    public Long getNubKey() {
        return nubKey;
    }

    public void setNubKey(Long nubKey) {
        this.nubKey = nubKey;
    }

    private Taxon genus;

    @ManyToOne
    @JoinColumn(name = "genus_id")
    public Taxon getGenus() {
        return genus;
    }

    public void setGenus(Taxon genus) {
        this.genus = genus;
    }

    private String species;

    @Column
    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    private String synonyms;

    @Column
    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    private OrganismType organismType;

    @ManyToOne
    @JoinColumn(name = "organism_type_id")
    public OrganismType getOrganismType() {
        return organismType;
    }

    public void setOrganismType(OrganismType organismType) {
        this.organismType = organismType;
    }

    private String gisdLink;

    @Column(name = "gisd_link")
    public String getGisdLink() {
        return gisdLink;
    }

    public void setGisdLink(String gisdLink) {
        this.gisdLink = gisdLink;
    }

    private String commonName;

    @Column(name = "common_name")
    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    private String authority;

    @Column
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    private String gbifJson;

    @Column(name = "gbif_json")
    public String getGbifJson() {
        return gbifJson;
    }

    public void setGbifJson(String gbifJson) {
        this.gbifJson = gbifJson;
    }

    private RedlistCategory redlistCategory;

    @ManyToOne
    @JoinColumn(name = "redlist_category_id")
    public RedlistCategory getRedlistCategory() {
        return redlistCategory;
    }

    public void setRedlistCategory(RedlistCategory redlistCategory) {
        this.redlistCategory = redlistCategory;
    }

    private Integer redlistId;

    @Column(name = "redlist_id")
    public Integer getRedlistId() {
        return redlistId;
    }

    public void setRedlistId(Integer redlistId) {
        this.redlistId = redlistId;
    }

    private Set<Biome> biomes;

    @ManyToMany
    @JoinTable(name = "ibis.species_biome", joinColumns = @JoinColumn(name = "species_id"), inverseJoinColumns = @JoinColumn(name = "biome_id"))
    public Set<Biome> getBiomes() {
        return biomes;
    }

    public void setBiomes(Set<Biome> biomes) {
        this.biomes = biomes;
    }

    private Set<Reference> references;

    @ManyToMany
    @JoinTable(name = "ibis.species_reference", joinColumns = @JoinColumn(name = "species_id"), inverseJoinColumns = @JoinColumn(name = "reference_id"))
    public Set<Reference> getReferences() {
        return references;
    }

    public void setReferences(Set<Reference> references) {
        this.references = references;
    }

    private String imageUrl;

    @Column(name = "image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private List<SpeciesSummary> speciesSummaries;

    @OneToMany(mappedBy = "species")
    @OrderBy("contentType.id ASC")
    public List<SpeciesSummary> getSpeciesSummaries() {
        return speciesSummaries;
    }

    public void setSpeciesSummaries(List<SpeciesSummary> speciesSummaries) {
        this.speciesSummaries = speciesSummaries;
    }

    /*
     * NOTE this only has references from the THREATENED species
     */
    private Set<SpeciesImpact> speciesImpacts;

    @OneToMany(mappedBy = "threatenedSpecies")
    public Set<SpeciesImpact> getSpeciesImpacts() {
        return speciesImpacts;
    }

    public void setSpeciesImpacts(Set<SpeciesImpact> speciesImpacts) {
        this.speciesImpacts = speciesImpacts;
    }

    @Override
    public String toString() {
        return name;
    }

    @Transient
    public String getScientificName() {
        return "<i>" + name + "</i> " + getAuthority();
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

        if (obj instanceof Species) {
            Species otherObj = (Species) obj;
            if (otherObj.getId().equals(this.getId())) {
                return true;
            }
            return false;
        }
        return super.equals(obj);
    }

}
