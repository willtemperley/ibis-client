package org.issg.ibis.responsive;

import org.jrc.ui.HtmlLabel;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

public class CopyOfHome extends VerticalLayout implements View {
	
	@Inject
	public CopyOfHome() {

		setSizeFull();
		
		addStyleName("home");

		HtmlLabel c = new HtmlLabel("<div>IBIS</div><div>island biodiversity & invasive species</div>");
		c.setWidth("200px");
		addComponent(c);
		Button c2 = new Button();
		c2.setIcon(FontAwesome.CHEVRON_DOWN);
		addComponent(c2);
		
		setExpandRatio(c, 1);
		
		setComponentAlignment(c2, Alignment.MIDDLE_CENTER);
		setComponentAlignment(c, Alignment.MIDDLE_CENTER);
		


//		addTab(new HtmlLabel(), "About");
//
//		addTab(new HtmlLabel(), "Search");
//
//		addTab(new HtmlLabel(), "API");
		

	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}
