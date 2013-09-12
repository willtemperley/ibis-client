package org.issg.ibis;

import org.issg.ibis.client.SpeciesSelector;
import org.jrc.persist.Dao;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

/**
 * An index with links to all views
 * 
 * @author Will Temperley
 *
 */
public class IndexPage extends VerticalLayout implements View {

	@Inject
	public IndexPage(Dao dao) {

	    SpeciesSelector ss = new SpeciesSelector(dao);
	    addComponent(ss);
	    
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
