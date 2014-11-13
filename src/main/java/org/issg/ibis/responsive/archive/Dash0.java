package org.issg.ibis.responsive.archive;

import org.issg.ibis.domain.Species;
import org.biopama.search.SpeciesSearch;
import org.jrc.edit.Dao;
import org.jrc.ui.HtmlLabel;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class Dash0 extends CssLayout implements View {


	/**
	 * 
	 */

	@Inject
	public Dash0(Dao dao) {
		setSizeFull();
        addStyleName("dashboard");
        Responsive.makeResponsive(this);

        SpeciesSearch sIAS = new SpeciesSearch(dao, Species.INVASIVE);
        SpeciesSearch sNat = new SpeciesSearch(dao, Species.NATIVE);

//		createPanel(sIAS, "Invasive Alien Species", "invasive-species");
//		createPanel(sNat, "Native Species", "native-species");
        createPanel(new LocationSearch(dao), "Search by Location", "location");

        createPanel(getAbout(), "About", "");
        createPanel(getPartners(), "Partners", "");
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
	Component createPanel(VerticalLayout c, String caption, String styleName) {
	    Panel panel = new Panel(caption);
        panel.addStyleName(styleName);
	
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