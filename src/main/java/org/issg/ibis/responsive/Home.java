package org.issg.ibis.responsive;

import org.jrc.ui.HtmlLabel;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TabSheet;

public class Home extends TabSheet implements View {
	
	@Inject
	public Home() {

		setSizeFull();

		addTab(new HtmlLabel(), "About");

		addTab(new HtmlLabel(), "Search");

		addTab(new HtmlLabel(), "API");
		

	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}
