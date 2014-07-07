package org.issg.ibis.domain.adapter;

import org.issg.ibis.domain.SpeciesLocation;

public class SpeciesLocationAdapter {

	private SpeciesLocation sl;

	public SpeciesLocationAdapter(SpeciesLocation sl) {
		this.sl = sl;
	}

	public String getSpecies() {
		return AdapterUtils.coalesce(sl.getSpecies().getName());
	}

	public String getRedlistCategory() {
		return AdapterUtils.coalesce(sl.getSpecies().getRedlistCategory());
	}

	public String getCommonName() {
		return AdapterUtils.coalesce(sl.getSpecies().getCommonName());
	}

	public String getCountry() {
		return AdapterUtils.coalesce(sl.getLocation().getCountry().getName());
	}

	public String getLocation() {
		return AdapterUtils.coalesce(sl.getLocation());
	}

	public String getBiologicalStatus() {
		return AdapterUtils.coalesce(sl.getBiologicalStatus());
	}

}
