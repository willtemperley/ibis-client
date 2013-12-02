package org.issg.ibis;

import org.issg.ibis.client.CountrySelector;
import org.issg.ibis.client.SpeciesSelector;
import org.jrc.form.editor.EditorPanelHeading;
import org.jrc.persist.Dao;
import org.jrc.ui.SimplePanel;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;

/**
 * An index with links to all views
 * 
 * @author Will Temperley
 *
 */
public class IndexPage extends HorizontalLayout implements View {

	private SpeciesSelector ss;

    @Inject
	public IndexPage(Dao dao) {

	    setSpacing(true);
	    setSizeFull();
	    {
	        SimplePanel sp = new SimplePanel();
//	        sp.setHeight("100%");
//	        sp.setSizeFull();
	        
	        sp.addComponent(new EditorPanelHeading("Search by species"));
	        addComponent(sp);
	        setComponentAlignment(sp, Alignment.MIDDLE_CENTER);
	        
	        ss = new SpeciesSelector(dao);
	        sp.addComponent(ss);


	    }
	    
	    {
	        SimplePanel sp = new SimplePanel();
	        addComponent(sp);
	        setComponentAlignment(sp, Alignment.MIDDLE_CENTER);
	        
	        sp.addComponent(new EditorPanelHeading("Search by Country"));
	        CountrySelector cs = new CountrySelector(dao);
	        sp.addComponent(cs);
	        
	    }
	}

	@Override
	public void enter(ViewChangeEvent event) {
	    String filterText = event.getParameters();
	    ss.setSearchText(filterText);
	}
}
