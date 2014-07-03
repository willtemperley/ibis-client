package org.issg.ibis.domain;

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
		return species.getName();
	}

	public String getCommonName() {
		return species.getCommonName();
	}

	public String getCountry() {
		return si.getLocation().getCountry().getName();
	}

	public String getImpactMechanism() {
		return si.getImpactMechanism().toString();
	}

	public String getImpactOutcome() {
		return si.getImpactOutcome().toString();
	}

	public String getBiologicalStatus() {
		return si.getBiologicalStatus().toString();
	}

	public String getLocation() {
		return si.getLocation().toString();
	}

	public Long getId() {
		return si.getId();
	}

}
