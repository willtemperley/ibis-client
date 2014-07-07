package org.issg.ibis.domain;

import java.util.ArrayList;
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

import org.issg.ibis.domain.adapter.SpeciesImpactAdapter;
import org.issg.ibis.domain.json.GbifSpecies;
import org.issg.ibis.domain.view.LocationView;

import com.google.gson.Gson;

@Entity
@NamedQueries({
		@NamedQuery(name = Species.NATIVE, query = "from Species s where exists (select 1 from SpeciesImpact i where i.nativeSpecies.id = s.id) order by name"),
		@NamedQuery(name = Species.INVASIVE, query = "from Species s where exists (select 1 from SpeciesImpact i where i.invasiveSpecies.id = s.id) order by name") })
@Table(schema = "ibis", name = "species")
public class Species {

	public static final String NATIVE = "Native";

	public static final String INVASIVE = "Invasive";

	public void populateFromJson() {

		String json = getGbifJson();

		if (json != null && !json.isEmpty()) {

			Gson g = new Gson();
			GbifSpecies gsp = g.fromJson(json, GbifSpecies.class);

			setKingdom(gsp.getKingdom());
			setPhylum(gsp.getPhylum());
			setClazz(gsp.getClazz());
			setOrder(gsp.getOrder());
			setFamily(gsp.getFamily());
			setGenus(gsp.getGenus());
		}
	}

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

	// private Taxon genus;
	//
	// @ManyToOne
	// @JoinColumn(name = "genus_id")
	// public Taxon getGenus() {
	// return genus;
	// }
	//
	// public void setGenus(Taxon genus) {
	// this.genus = genus;
	// }

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

	private Set<SpeciesLocation> speciesLocations;

	@OneToMany(mappedBy = "species")
	public Set<SpeciesLocation> getSpeciesLocations() {
		return speciesLocations;
	}

	public void setSpeciesLocations(Set<SpeciesLocation> speciesLocations) {
		this.speciesLocations = speciesLocations;
	}

	private Set<LocationView> speciesLocationViews;

	@OneToMany(mappedBy = "species")
	public Set<LocationView> getSpeciesLocationViews() {
		return speciesLocationViews;
	}

	public void setSpeciesLocationViews(Set<LocationView> speciesLocationViews) {
		this.speciesLocationViews = speciesLocationViews;
	}

	private Set<SpeciesImpact> nativeSpeciesImpacts;

	@OneToMany(mappedBy = "invasiveSpecies")
	public Set<SpeciesImpact> getNativeSpeciesImpacts() {
		return nativeSpeciesImpacts;
	}

	public void setNativeSpeciesImpacts(Set<SpeciesImpact> nativeSpeciesImpacts) {
		this.nativeSpeciesImpacts = nativeSpeciesImpacts;
	}

	@Transient
	public boolean getIsInvasive() {

		Set<SpeciesImpact> isi = getNativeSpeciesImpacts();
		if (isi != null && isi.size() > 0) {
			return true;
		}

		return false;
	}

	/**
	 * Builds a list of all impacts
	 * 
	 * @return
	 */
	@Transient
	public List<SpeciesImpactAdapter> getSpeciesImpactAdapters() {

		ArrayList<SpeciesImpactAdapter> sia = new ArrayList<SpeciesImpactAdapter>();

		Set<SpeciesImpact> nsi = getNativeSpeciesImpacts();
		Set<SpeciesImpact> isi = getInvasiveSpeciesImpacts();

		if (nsi != null && nsi.size() > 0) {
			for (SpeciesImpact speciesImpact : nsi) {
				sia.add(new SpeciesImpactAdapter(speciesImpact, false));
			}
		} else if (isi != null && isi.size() > 0) {
			for (SpeciesImpact speciesImpact : isi) {
				sia.add(new SpeciesImpactAdapter(speciesImpact, true));
			}
		}
		return sia;
	}

	private Set<SpeciesImpact> invasiveSpeciesImpacts;

	@OneToMany(mappedBy = "nativeSpecies")
	public Set<SpeciesImpact> getInvasiveSpeciesImpacts() {
		return invasiveSpeciesImpacts;
	}

	public void setInvasiveSpeciesImpacts(
			Set<SpeciesImpact> invasiveSpeciesImpacts) {
		this.invasiveSpeciesImpacts = invasiveSpeciesImpacts;
	}

	private String link;

	@Column
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	private String kingdom;

	@Column
	public String getKingdom() {
		return kingdom;
	}

	public void setKingdom(String kingdom) {
		this.kingdom = kingdom;
	}

	private String phylum;

	@Column
	public String getPhylum() {
		return phylum;
	}

	public void setPhylum(String phylum) {
		this.phylum = phylum;
	}

	private String clazz;

	@Column
	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	private String order;

	@Column(name = "order_")
	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	private String family;

	@Column
	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	private String genus;

	@Column
	public String getGenus() {
		return genus;
	}

	public void setGenus(String genus) {
		this.genus = genus;
	}

	@Override
	public String toString() {
		return name;
	}

	private String scientificName;

	@Transient
	public String getScientificName() {
		if (scientificName == null) {
			StringBuilder sb = new StringBuilder();
			sb.append("<i>");
			sb.append(name);
			sb.append("</i> ");
			if (getAuthority() != null) {
				sb.append(getAuthority());
			}
			scientificName = sb.toString();
		}
		return scientificName;
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
