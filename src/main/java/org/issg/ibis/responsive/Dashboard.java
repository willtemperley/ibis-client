package org.issg.ibis.responsive;

import org.issg.ibis.ViewModule;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;
import org.jrc.edit.Dao;
import org.jrc.ui.HtmlLabel;
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
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
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

        LocationSearch2 lSearch = new LocationSearch2(dao);
		mainContent.addComponent(lSearch);
		
		lSearch.addMValueChangeListener(new MValueChangeListener<Location>() {
			
			@Override
			public void valueChange(MValueChangeEvent<Location> event) {
				Location val = event.getValue();
				if (val != null) {
					Navigator nav = UI.getCurrent().getNavigator();
					nav.navigateTo(ViewModule.LOCATION_PERSPECTIVE + "/" + val.getId());
				}
			}
		});

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