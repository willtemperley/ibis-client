package org.issg.ibis.domain.json;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class GbifSpecies {

	@Expose
	private Boolean synonym;
	@Expose
	private List<Identifier> identifiers = new ArrayList<Identifier>();
	@Expose
	private Long classKey;
	@Expose
	private Long numDescendants;
	@Expose
	private Long kingdomKey;
	@Expose
	private String sourceId;
	@Expose
	private String nameType;
	@Expose
	private String phylum;
	@Expose
	private String rank;
	@Expose
	private String order;
	@Expose
	private String kingdom;
	@Expose
	private String authorship;
	@Expose
	private Long phylumKey;
	@Expose
	private String family;
	@Expose
	private String datasetKey;
	@Expose
	private Long key;
	@Expose
	private String scientificName;
	@Expose
	private String species;
	@Expose
	private Long parentKey;
	@Expose
	private Long familyKey;
	@Expose
	private String origin;
	@Expose
	private List<Object> nomenclaturalStatus = new ArrayList<Object>();
	@Expose
	private String parent;
	@Expose
	private String genus;
	@Expose
	private Long speciesKey;
	@Expose
	private String canonicalName;
	@Expose
	private Long genusKey;
	@Expose
	private Long orderKey;
	@Expose
	private String clazz;
	@Expose
	private Long nubKey;

	public Boolean getSynonym() {
		return synonym;
	}

	public void setSynonym(Boolean synonym) {
		this.synonym = synonym;
	}

	public List<Identifier> getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(List<Identifier> identifiers) {
		this.identifiers = identifiers;
	}

	public Long getClassKey() {
		return classKey;
	}

	public void setClassKey(Long classKey) {
		this.classKey = classKey;
	}

	public Long getNumDescendants() {
		return numDescendants;
	}

	public void setNumDescendants(Long numDescendants) {
		this.numDescendants = numDescendants;
	}

	public Long getKingdomKey() {
		return kingdomKey;
	}

	public void setKingdomKey(Long kingdomKey) {
		this.kingdomKey = kingdomKey;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getNameType() {
		return nameType;
	}

	public void setNameType(String nameType) {
		this.nameType = nameType;
	}

	public String getPhylum() {
		return phylum;
	}

	public void setPhylum(String phylum) {
		this.phylum = phylum;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getKingdom() {
		return kingdom;
	}

	public void setKingdom(String kingdom) {
		this.kingdom = kingdom;
	}

	public String getAuthorship() {
		return authorship;
	}

	public void setAuthorship(String authorship) {
		this.authorship = authorship;
	}

	public Long getPhylumKey() {
		return phylumKey;
	}

	public void setPhylumKey(Long phylumKey) {
		this.phylumKey = phylumKey;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getDatasetKey() {
		return datasetKey;
	}

	public void setDatasetKey(String datasetKey) {
		this.datasetKey = datasetKey;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getScientificName() {
		return scientificName;
	}

	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public Long getParentKey() {
		return parentKey;
	}

	public void setParentKey(Long parentKey) {
		this.parentKey = parentKey;
	}

	public Long getFamilyKey() {
		return familyKey;
	}

	public void setFamilyKey(Long familyKey) {
		this.familyKey = familyKey;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public List<Object> getNomenclaturalStatus() {
		return nomenclaturalStatus;
	}

	public void setNomenclaturalStatus(List<Object> nomenclaturalStatus) {
		this.nomenclaturalStatus = nomenclaturalStatus;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getGenus() {
		return genus;
	}

	public void setGenus(String genus) {
		this.genus = genus;
	}

	public Long getSpeciesKey() {
		return speciesKey;
	}

	public void setSpeciesKey(Long speciesKey) {
		this.speciesKey = speciesKey;
	}

	public String getCanonicalName() {
		return canonicalName;
	}

	public void setCanonicalName(String canonicalName) {
		this.canonicalName = canonicalName;
	}

	public Long getGenusKey() {
		return genusKey;
	}

	public void setGenusKey(Long genusKey) {
		this.genusKey = genusKey;
	}

	public Long getOrderKey() {
		return orderKey;
	}

	public void setOrderKey(Long orderKey) {
		this.orderKey = orderKey;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public Long getNubKey() {
		return nubKey;
	}

	public void setNubKey(Long nubKey) {
		this.nubKey = nubKey;
	}

}