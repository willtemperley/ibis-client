package org.issg.ibis.perspective.species;

import java.util.List;

import org.issg.ibis.client.content.SimpleContentController;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesSummary;
import org.issg.ibis.webservices.ArkiveV1Search;
import org.jrc.ui.HtmlHeader;
import org.jrc.ui.HtmlLabel;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

public class SpeciesSummaryController extends CssLayout {

	private HtmlHeader speciesName = new HtmlHeader();
	private HtmlHeader speciesSecondaryName = new HtmlHeader();
	private HtmlLabel speciesImage = new HtmlLabel();
	private HtmlLabel redlistStatus = new HtmlLabel();

	private Taxonomy taxonomy = new Taxonomy();
	private ArkiveV1Search arkiveSearch;
	
	private SimpleContentController simpleContentController ;

	public SpeciesSummaryController(ArkiveV1Search arkiveSearch) {


		this.arkiveSearch = arkiveSearch;
		this.simpleContentController = new SimpleContentController(this);

		addComponent(speciesName);
		addComponent(speciesSecondaryName);
		speciesName.addStyleName("header-large");

		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(speciesImage);
		hl.addComponent(taxonomy);
		// panel.addComponent(speciesImage);
		// panel.addComponent(taxonomy);
		addComponent(hl);
		hl.setWidth("100%");
		addComponent(redlistStatus);
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

		List<SpeciesSummary> ss = sp.getSpeciesSummaries();
		simpleContentController.setContent(ss);


		if (sp.getRedlistCategory() != null) {
			String redlistImgUrl = String
					.format("<img src='/ibis-client/VAADIN/themes/dashboard/redlist/240px-Status_iucn3.1_%s.svg.png'/>",
							sp.getRedlistCategory().getRedlistCode());
			redlistStatus.setValue(redlistImgUrl);
		} else {
			redlistStatus.setValue("");
		}

	}


}
