package org.issg.ibis.domain.adapter;

import org.issg.ibis.domain.SpeciesImpact;

public class IASAdapter {
	
	private SpeciesImpact si;

	public IASAdapter(SpeciesImpact si) {
		this.si = si; 
	}

	public String getNativeSpecies() {
		return AdapterUtils.coalesce( si.getNativeSpecies().getName());
	}

	public String getNativeCommonName() {
		return AdapterUtils.coalesce( si.getNativeSpecies().getCommonName());
	}

	public String getInvasiveAlienSpecies() {
		return AdapterUtils.coalesce( si.getInvasiveSpecies().getName());
	}

	public String getInvasiveAlienCommonName() {
		return AdapterUtils.coalesce( si.getInvasiveSpecies().getCommonName());
	}

	public String getImpactMechanism() {
		return AdapterUtils.coalesce( si.getImpactMechanism());
	}

	public String getImpactOutcome() {
		return AdapterUtils.coalesce( si.getImpactOutcome());
	}

	public Long getId() {
		return si.getId();
	}
	
	public String getReference() {
		return AdapterUtils.coalesce(si.getReference());
	}

}
