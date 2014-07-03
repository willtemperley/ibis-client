package org.issg.ibis.responsive;

import org.jrc.persist.Dao;
import org.jrc.ui.HtmlLabel;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class Dashboard extends CssLayout implements View {


	/**
	 * 
	 */

	@Inject
	public Dashboard(Dao dao) {
		setSizeFull();
        addStyleName("dashboard");
        Responsive.makeResponsive(this);

        createPanel(new SpeciesSearch(dao), "Search by Species");
        createPanel(new LocationSearch(dao), "Search by Location");

        createPanel(getAbout(), "About");
        createPanel(getPartners(), "Partners");
    }

	private VerticalLayout getAbout() {
		VerticalLayout vl = new VerticalLayout();
        HtmlLabel c = new HtmlLabel("IBIS is focused on the threat of invasive alien species on native species and ecosystems on islands.");
        c.setWidth("100%");
		vl.addComponent(c);
		return vl;
	}

	private VerticalLayout getPartners() {
		VerticalLayout vl = new VerticalLayout();
        HtmlLabel c = new HtmlLabel("Partners and supporters goes here");
        c.setWidth("100%");
		vl.addComponent(c);
		return vl;
	}
	Component createPanel(VerticalLayout c, String string) {
	    Panel panel = new Panel(string);
	    panel.setSizeUndefined();
	
	    c.setMargin(true);
	    c.setSizeFull();
	    panel.setContent(c);
	    
	    addComponent(panel);
	
	    return panel;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}