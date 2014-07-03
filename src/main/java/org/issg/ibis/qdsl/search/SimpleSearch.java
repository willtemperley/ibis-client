package org.issg.ibis.qdsl.search;

import org.issg.ibis.responsive.LocationSearch;
import org.issg.ibis.responsive.SpeciesSearch;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

public class SimpleSearch extends CssLayout implements View {


	@Override
	public void enter(ViewChangeEvent event) {
	}

	@Inject
	public SimpleSearch(SpeciesSearch ss, LocationSearch ls) {

		addComponent(ss);
		addComponent(ls);
		addComponent(new Logos());
		setWidth("100%");
		addStyleName("grid");

		Responsive.makeResponsive(this);
	}
	
	@Override
	public void addComponent(Component c) {
		c.setSizeUndefined();
		super.addComponent(c);
	}

}