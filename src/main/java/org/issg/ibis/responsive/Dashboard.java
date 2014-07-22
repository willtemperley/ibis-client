package org.issg.ibis.responsive;

import org.issg.ibis.domain.Species;
import org.jrc.edit.Dao;
import org.jrc.ui.HtmlLabel;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class Dashboard extends VerticalLayout implements View {

	/**
	 * 
	 */
	@Inject
	public Dashboard(Dao dao) {
		setSizeFull();

		setSpacing(true);
        addStyleName("dash2");
        Responsive.makeResponsive(this);

        SpeciesSearch2 sIAS = new SpeciesSearch2(dao, Species.INVASIVE);
        SpeciesSearch2 sNat = new SpeciesSearch2(dao, Species.NATIVE);
        LocationSearch2 lSearch = new LocationSearch2(dao);

        HtmlLabel c = new HtmlLabel("IBIS aims to provide free and accurate information on the threats to island biodiversity and ecosystems posed by invasive alien species.");
        c.setWidth("100%");
        c.setHeight("40px");
		addComponent(c);
		c.addStyleName("home-text");
		
		HorizontalLayout mainContent = new HorizontalLayout();
		mainContent.setSizeFull();
		mainContent.setSpacing(true);
		addComponent(mainContent);
		
		setExpandRatio(mainContent, 1);
		
		VerticalLayout spLayout = new VerticalLayout();
		spLayout.setSizeFull();
		spLayout.setSpacing(true);
		spLayout.addComponent(createPanel(sNat, "Native Species", "native-species"));
		spLayout.addComponent(createPanel(sIAS, "Invasive Alien Species", "invasive-species"));
		mainContent.addComponent(spLayout);

		Component locPanel = createPanel(lSearch, "Search by Location", "location");
		mainContent.addComponent(locPanel);

//        createPanel(getAbout(), "About", "");
//        createPanel(getPartners(), "Partners", "");
    }


	Component createPanel(AbstractOrderedLayout content, String caption, String styleName) {
	    Panel panel = new Panel(caption);
        panel.addStyleName(styleName);
	    content.setMargin(true);
	    panel.setContent(content);
		panel.setSizeFull();
//	    addComponent(panel);
	    return panel;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}