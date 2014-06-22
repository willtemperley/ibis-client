package org.issg.ibis.perspective.species;

import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.OrganismType;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.webservices.ArkiveV1Search;
import org.jrc.ui.HtmlHeader;
import org.jrc.ui.HtmlLabel;
import org.jrc.ui.SimplePanel;

public class SpeciesImpactPanel extends SimplePanel {

	public HtmlHeader name = new HtmlHeader();
	public HtmlLabel commonName = new HtmlLabel();
	public HtmlLabel organismType = new HtmlLabel();
	public HtmlLabel location = new HtmlLabel();
	public HtmlLabel image = new HtmlLabel();

	public SpeciesImpactPanel() {
		setSizeFull();
		addComponent(name);
		addComponent(commonName);
		commonName.setCaption("Common name:");
		addComponent(organismType);
		organismType.setCaption("Organism type:");
		location.setCaption("Location:");
		addComponent(image);
		addComponent(organismType);
		addComponent(location);
	}

	public void setSpeciesImpact(SpeciesImpact si) {
		Species invasiveSpecies = si.getInvasiveSpecies();
		name.setValue(invasiveSpecies.getName());
		commonName.setValue(invasiveSpecies.getName());

		OrganismType ot = invasiveSpecies.getOrganismType();
		if (ot != null) {
			organismType.setValue(ot.getLabel());
		} else {
			organismType.setValue(null);
		}
//		String speciesImage = ArkiveV1Search.getSpeciesImage(invasiveSpecies.getName());
//		image.setValue(speciesImage);
		
		Location loc = si.getLocation();
		location.setValue(loc.getName());
	}
}
