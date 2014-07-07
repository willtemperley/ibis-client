package org.issg.ibis.domain.adapter;

import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;

public class SpeciesImpactAdapter {

	private SpeciesImpact si;
	private Species species;

	/**
	 * Acts as a single method of displaying a species impact through adapting
	 * whether it should display an impact of an invasive on a native sp or vice
	 * versa.
	 * 
	 * @param si
	 * @param isInvasiveImpact
	 */
	public SpeciesImpactAdapter(SpeciesImpact si, boolean isInvasiveImpact) {
		this.si = si;

		if (isInvasiveImpact) {
			this.species = si.getInvasiveSpecies();
		} else {
			this.species = si.getNativeSpecies();
		}

	}

	public String getName() {
		return AdapterUtils.coalesce(species.getName());
	}

	public String getCommonName() {
		return AdapterUtils.coalesce(species.getCommonName());
	}

	public String getCountry() {
		return AdapterUtils.coalesce(si.getLocation().getCountry());
	}

	public String getImpactMechanism() {
		return AdapterUtils.coalesce( si.getImpactMechanism());
	}

	public String getImpactOutcome() {
		return AdapterUtils.coalesce( si.getImpactOutcome());
	}

	public String getBiologicalStatus() {
		return AdapterUtils.coalesce( si.getBiologicalStatus());
	}

	public String getLocation() {
		return AdapterUtils.coalesce(si.getLocation());
	}

	public Long getId() {
		return si.getId();
	}
	
	public String getReference() {
		return AdapterUtils.coalesce(si.getReference());
	}

}
