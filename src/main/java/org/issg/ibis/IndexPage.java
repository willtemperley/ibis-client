package org.issg.ibis;

import it.jrc.form.editor.EditorPanelHeading;

import org.issg.excel.download.Exporter;
import org.jrc.persist.Dao;
import org.jrc.ui.HtmlLabel;
import org.jrc.ui.SimplePanel;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

/**
 * An index with links to all views
 * 
 * @author Will Temperley
 *
 */
public class IndexPage extends HorizontalLayout implements View {


    @Inject
	public IndexPage(Dao dao) {

	    setSpacing(true);
	    setSizeFull();
	    {
	        SimplePanel sp = new SimplePanel();
//	        sp.setHeight("100%");
//	        sp.setSizeFull();
	        
	        sp.addComponent(new EditorPanelHeading("About us"));
	        sp.addComponent(new HtmlLabel("IBIS is focused on the threat of invasive alien species on native species and ecosystems on islands."));
	        
	        
	        addComponent(sp);

	        setComponentAlignment(sp, Alignment.MIDDLE_CENTER);
	        

	    }
	    
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}
}
