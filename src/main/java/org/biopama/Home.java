package org.biopama;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;

import org.biopama.ibis.ViewModule;
import org.biopama.ui.HtmlLabel;

import java.util.ArrayList;
import java.util.List;

public class Home extends VerticalLayout implements View {
	
	@Inject
	public Home() {

		setSizeFull();
		
		setMargin(false);

//		HtmlLabel c = new HtmlLabel("<div>IBIS</div><div>island biodiversity & invasive species</div>");
		String text = "<div class='ibis-logo'>IBIS</div><div class='ibis-text'><span class='bio-text'>island biodiversity</span>&nbsp;<span class='inv-text'>invasive species</span></div>";
		HtmlLabel l = new HtmlLabel(text);
		l.setSizeFull();
		l.addStyleName("content");
		l.addStyleName("big-home-logo");
		addComponent(l);

		VerticalLayout middleVL = new VerticalLayout();
		addComponent(middleVL);
		HtmlLabel descr = new HtmlLabel();
		descr.setValue("IBIS aims to record and provide information on the occurrence, biological status and impacts of invasive alien species on native species on islands, with a focus on those that are classified as ‘threatened’ in the IUCN Red List of Threatened Species.");
		descr.addStyleName("home-middle");
		descr.setSizeFull();
		middleVL.addComponent(descr);
		Button search = new Button("SEARCH");
		search.addStyleName("search-button");
		search.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				UI.getCurrent().getNavigator().navigateTo(ViewModule.SEARCH);
			}
		});
		middleVL.addComponent(search);
		middleVL.setComponentAlignment(search, Alignment.MIDDLE_CENTER);


		HorizontalLayout hl = new HorizontalLayout();
		hl.addStyleName("content");
		hl.setMargin(true);
		hl.setSizeFull();

		List<String> logos = new ArrayList<String>();
		logos.add("issg-logo");
		logos.add("ec-logo");
		logos.add("iucn-logo");

		for (String name: logos) {
			Label logoLabel = new Label();
			logoLabel.setStyleName(name);
			hl.addComponent(logoLabel);
		}
		addComponent(hl);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}
