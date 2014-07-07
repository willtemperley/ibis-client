package org.issg.ibis.perspective.species;

import java.util.List;

import org.issg.ibis.client.content.SimpleContentPanel;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesSummary;
import org.issg.ibis.webservices.ArkiveV1Search;
import org.jrc.ui.HtmlHeader;
import org.jrc.ui.HtmlLabel;
import org.jrc.ui.SimplePanel;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

public class SpeciesSummaryController extends SimpleContentPanel {

	private HtmlHeader speciesName = new HtmlHeader();
	private HtmlHeader speciesSecondaryName = new HtmlHeader();
	private HtmlLabel speciesImage = new HtmlLabel();
	private HtmlLabel redlistStatus = new HtmlLabel();

	private Taxonomy taxonomy = new Taxonomy();
	private ArkiveV1Search arkiveSearch;

	public SpeciesSummaryController(SimplePanel panel,
			ArkiveV1Search arkiveSearch) {

		super(panel);

		this.arkiveSearch = arkiveSearch;

		panel.addComponent(speciesName);
		panel.addComponent(speciesSecondaryName);
		speciesName.addStyleName("header-large");

		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(speciesImage);
		hl.addComponent(taxonomy);
		// panel.addComponent(speciesImage);
		// panel.addComponent(taxonomy);
		panel.addComponent(hl);
		hl.setWidth("100%");
		panel.addComponent(redlistStatus);
	}

	public void setSpecies(Species sp) {

		String img = arkiveSearch.getSpeciesImage(sp.getName());

		String commonName = sp.getCommonName();

		if (commonName == null || commonName.isEmpty()) {
			speciesName.setValue(sp.getScientificName());
			speciesSecondaryName.setVisible(false);
		} else {
			speciesName.setValue(commonName);
			speciesSecondaryName.setVisible(true);
			speciesSecondaryName.setValue(sp.getScientificName());
		}

		speciesImage.setValue(img);

		taxonomy.setSpecies(sp);

		// Clear content
		for (Component cc : contentComponents) {
			panel.removeComponent(cc);
		}
		contentComponents.clear();

		List<SpeciesSummary> ss = sp.getSpeciesSummaries();

		setContent(ss);

		// commonName.setValue(sp.getCommonName());

		// if (sp.getRedlistCategory() != null) {
		// String code = sp.getRedlistCategory().getRedlistCode();
		// redlistStatus.setValue(code);
		//
		// }

		if (sp.getRedlistCategory() != null) {
			String redlistImgUrl = String
					.format("<img src='/ibis-client/VAADIN/themes/dashboard/redlist/240px-Status_iucn3.1_%s.svg.png'/>",
							sp.getRedlistCategory().getRedlistCode());
			redlistStatus.setValue(redlistImgUrl);
		} else {
			redlistStatus.setValue("");
		}

	}

	public void setLocation(Location location) {

	}

}
