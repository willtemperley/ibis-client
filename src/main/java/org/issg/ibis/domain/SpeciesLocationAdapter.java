package org.issg.ibis.domain;

public class SpeciesLocationAdapter {
	
	private SpeciesLocation sl;

	public SpeciesLocationAdapter(SpeciesLocation sl) {
		this.sl = sl;
	}
	
	public String getCountry() {
		return sl.getLocation().getCountry().getName();
	}
	
	public String getLocation() {
		return sl.getLocation().toString();
	}
	
	public String getBiologicalStatus() {
		return sl.getBiologicalStatus().toString();
	}

}
