package org.issg.ibis;

import org.issg.ibis.client.SpeciesSelector;
import org.issg.ibis.display.SimplePanel;
import org.jrc.form.editor.EditorPanelHeading;
import org.jrc.persist.Dao;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

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
	    
//	    {
//	        SimplePanel sp = new SimplePanel();
//	        sp.setSizeFull();
//	        
//	        sp.addComponent(new EditorPanelHeading("Search by location"));
//	        addComponent(sp);
//	        
//	    }
	}

	@Override
	public void enter(ViewChangeEvent event) {
	    String filterText = event.getParameters();
	    ss.setSearchText(filterText);
	}
}
