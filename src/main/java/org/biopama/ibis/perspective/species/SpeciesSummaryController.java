package org.biopama.ibis.perspective.species;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.biopama.ibis.perspective.shared.SimpleContentController;
import org.biopama.ibis.webservices.ArkiveV1Search;
import org.biopama.ui.HtmlHeader;
import org.biopama.ui.HtmlLabel;
import org.issg.ibis.domain.Content;
import org.issg.ibis.domain.ContentType;
import org.issg.ibis.domain.Reference;
import org.issg.ibis.domain.Species;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

public class SpeciesSummaryController extends CssLayout {

	private HtmlHeader speciesName = new HtmlHeader();
	private HtmlHeader speciesSecondaryName = new HtmlHeader();
	private HtmlLabel speciesImage = new HtmlLabel();
	private HtmlLabel redlistStatus = new HtmlLabel();

	private Taxonomy taxonomy = new Taxonomy();
	private ArkiveV1Search arkiveSearch;

	private SimpleContentController simpleContentController;

	public SpeciesSummaryController(ArkiveV1Search arkiveSearch) {

		setSizeFull();
		this.arkiveSearch = arkiveSearch;
		this.simpleContentController = new SimpleContentController(this);

		this.addComponent(speciesName);
		this.addComponent(speciesSecondaryName);
		speciesName.addStyleName("header-large");

		HorizontalLayout hl = new HorizontalLayout();
		hl.setWidth("100%");
		hl.addComponent(speciesImage);
		hl.addComponent(taxonomy);
		this.addComponent(hl);
		this.addComponent(redlistStatus);
	}

	class ReferenceContent implements Content {

		private ContentType ct;
		private String content;

		public ReferenceContent() {
			ct = new ContentType();
			ct.setName("References");
		}

		@Override
		public ContentType getContentType() {
			return ct;
		}

		@Override
		public void setContentType(ContentType contentType) {
		}

		@Override
		public String getContent() {
			return content;
		}

		@Override
		public void setContent(String content) {
			this.content = content;
		}

	}

	public void setSpecies(Species sp) {

		String img = arkiveSearch.getSpeciesImage(sp.getName());

		String commonName = sp.getCommonName();

		if (commonName == null || commonName.isEmpty()) {
			speciesName.setValue(sp.getScientificName());
			speciesSecondaryName.setVisible(false);
			speciesName.addStyleName("content-header");
			speciesSecondaryName.removeStyleName("content-header");
		} else {
			speciesName.setValue(commonName);
			speciesSecondaryName.setVisible(true);
			speciesSecondaryName.setValue(sp.getScientificName());
			speciesName.removeStyleName("content-header");
			speciesSecondaryName.addStyleName("content-header");
		}

		speciesImage.setValue(img);

		taxonomy.setSpecies(sp);

		List<? extends Content> ss = sp.getSpeciesSummaries();
		List<Content> contentSections = new ArrayList<Content>();
		contentSections.addAll(ss);

		Set<Reference> refs = sp.getReferences();
		if (refs != null && refs.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (Reference reference : refs) {
				sb.append("<div>");
				sb.append(reference.getContent());
				sb.append("</div>");
			}
			Content rc = new ReferenceContent();
			rc.setContent(sb.toString());
			contentSections.add(rc);
		}

		simpleContentController.setContent(contentSections);

		if (sp.getConservationClassification() != null) {
			String redlistImgUrl = String
					.format("<img src='/ibis-client/VAADIN/themes/responsive/redlist/240px-Status_iucn3.1_%s.svg.png'/>",
							sp.getConservationClassification().getAbbreviation());
			redlistStatus.setValue(redlistImgUrl);
		} else {
			redlistStatus.setValue("");
		}

	}

}
