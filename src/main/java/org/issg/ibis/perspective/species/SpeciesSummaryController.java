package org.issg.ibis.perspective.species;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.issg.ibis.client.content.SimpleContentController;
import org.issg.ibis.domain.Content;
import org.issg.ibis.domain.ContentType;
import org.issg.ibis.domain.Reference;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesSummary;
import org.issg.ibis.webservices.ArkiveV1Search;
import org.jrc.ui.HtmlHeader;
import org.jrc.ui.HtmlLabel;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

public class SpeciesSummaryController extends Panel {

	private HtmlHeader speciesName = new HtmlHeader();
	private HtmlHeader speciesSecondaryName = new HtmlHeader();
	private HtmlLabel speciesImage = new HtmlLabel();
	private HtmlLabel redlistStatus = new HtmlLabel();

	private Taxonomy taxonomy = new Taxonomy();
	private ArkiveV1Search arkiveSearch;

	private SimpleContentController simpleContentController;

	public SpeciesSummaryController(ArkiveV1Search arkiveSearch) {

		CssLayout l = new CssLayout();
		this.setContent(l);
		l.setSizeFull();
		this.arkiveSearch = arkiveSearch;
		this.simpleContentController = new SimpleContentController(l);

		l.addComponent(speciesName);
		l.addComponent(speciesSecondaryName);
		speciesName.addStyleName("header-large");

		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(speciesImage);
		hl.addComponent(taxonomy);
		// panel.addComponent(speciesImage);
		// panel.addComponent(taxonomy);
		l.addComponent(hl);
		hl.setWidth("100%");
		l.addComponent(redlistStatus);
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
//			setCaption(sp.getScientificName());
			speciesSecondaryName.setVisible(false);
		} else {
			speciesName.setValue(commonName);
//			setCaption(commonName);
			speciesSecondaryName.setVisible(true);
			speciesSecondaryName.setValue(sp.getScientificName());
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
				sb.append(reference.getContent());
			}
			Content rc = new ReferenceContent();
			rc.setContent(sb.toString());
			contentSections.add(rc);
		}

		simpleContentController.setContent(contentSections);

		if (sp.getRedlistCategory() != null) {
			String redlistImgUrl = String
					.format("<img src='/ibis-client/VAADIN/themes/responsive/redlist/240px-Status_iucn3.1_%s.svg.png'/>",
							sp.getRedlistCategory().getRedlistCode());
			redlistStatus.setValue(redlistImgUrl);
		} else {
			redlistStatus.setValue("");
		}

	}

}
