package org.biopama.search;

import org.issg.ibis.ViewModule;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;
import org.jrc.edit.Dao;
import org.vaadin.maddon.fields.MValueChangeEvent;
import org.vaadin.maddon.fields.MValueChangeListener;

import com.google.inject.Inject;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class Search extends VerticalLayout implements View {

	@Inject
	public Search(Dao dao) {
		setSizeFull();
		addStyleName("content");

		setSpacing(true);
		setMargin(true);
		Responsive.makeResponsive(this);

		SpeciesSearch sIAS = new SpeciesSearch(dao, Species.INVASIVE);
		SpeciesSearch sNat = new SpeciesSearch(dao, Species.NATIVE);


		HorizontalLayout mainContent = new HorizontalLayout();
		mainContent.setSizeFull();
		mainContent.setSpacing(true);
		addComponent(mainContent);

		setExpandRatio(mainContent, 1);

		VerticalLayout spLayout = new VerticalLayout();
		spLayout.setSizeFull();
		spLayout.setSpacing(true);
		spLayout.addComponent(stylePanel(sNat, "native-species"));
		spLayout.addComponent(stylePanel(sIAS, "invasive-species"));
		mainContent.addComponent(spLayout);

		LocationSearch lSearch = new LocationSearch(dao);
		stylePanel(lSearch, "location");

		mainContent.addComponent(lSearch);

		lSearch.addMValueChangeListener(new MValueChangeListener<Location>() {

			@Override
			public void valueChange(MValueChangeEvent<Location> event) {
				Location val = event.getValue();
				if (val != null) {
					Navigator nav = UI.getCurrent().getNavigator();
					nav.navigateTo(ViewModule.LOCATION_PERSPECTIVE + "/"
							+ val.getId());
				}
			}
		});

		// stylePanel(getAbout(), "About", "");
		// stylePanel(getPartners(), "Partners", "");
	}

	Component stylePanel(AbstractOrderedLayout panel, String styleName) {
		panel.addStyleName(styleName);
		panel.addStyleName("facet-container");
		panel.setMargin(true);
		panel.setSizeFull();
		return panel;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
}