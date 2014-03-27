package org.issg.ibis.perspective.species;

import org.issg.ibis.domain.SpeciesImpact;
import org.jrc.ui.HtmlHeader;
import org.jrc.ui.HtmlLabel;
import org.jrc.ui.SimplePanel;

import com.vaadin.ui.Panel;

public class MapLegend extends Panel {
	
	private SpeciesImpactPanel speciesImpactPanel = new SpeciesImpactPanel();

	private SimplePanel overviewMapLegend = new SimplePanel();
	
	public HtmlLabel descr = new HtmlLabel();

	
	public MapLegend() {
		
//		buildSpeciesInfo();
		buildOverviewMapLegend();
		
		setContent(overviewMapLegend);

		setSizeFull();
	}


	private void buildOverviewMapLegend() {
		overviewMapLegend.setSizeFull();
		overviewMapLegend.addComponent(descr);
		descr.setValue("Map information goes here");
	}
	
	public void showMapLegend() {
		setContent(overviewMapLegend);
	}

	public void showSpeciesImpact(SpeciesImpact si) {
		setContent(speciesImpactPanel);
		speciesImpactPanel.setSpeciesImpact(si);
	}

}
