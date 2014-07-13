package org.issg.ibis;

import it.jrc.form.editor.EditorPanelHeading;

import org.jrc.edit.Dao;
import org.jrc.ui.HtmlLabel;
import org.jrc.ui.SimplePanel;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

/**
 * An index with links to all views
 * 
 * @author Will Temperley
 *
 */
public class IndexPage extends Panel implements View {


    @Inject
	public IndexPage(Dao dao) {

	    setSizeFull();
	    
		CssLayout grid = new CssLayout();
		grid.setWidth("100%");
		grid.addStyleName("grid");
		setContent(grid);

		for (int i = 1; i < 10; i++) {
			Label l = new Label("" + i);
			l.setSizeUndefined();
			grid.addComponent(l);
		}
		
		Responsive.makeResponsive(this);
		
	    {
//	        SimplePanel sp = new SimplePanel();
////	        sp.setHeight("100%");
////	        sp.setSizeFull();
//	        
//	        sp.addComponent(new EditorPanelHeading("About us"));
//	        sp.addComponent(new HtmlLabel("IBIS is focused on the threat of invasive alien species on native species and ecosystems on islands."));
//	        
//	        addComponent(sp);
//
//	        setComponentAlignment(sp, Alignment.MIDDLE_CENTER);
	        

	    }
	    
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}
}
