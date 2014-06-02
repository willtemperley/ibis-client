package org.jrc.server.webservices;

import org.issg.ibis.domain.SpeciesImpact;

public interface SpeciesOutputWriter {
	
	abstract void writeRow(SpeciesImpact si);

}
