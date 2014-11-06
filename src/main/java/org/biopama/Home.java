package org.biopama;

import org.jrc.ui.HtmlLabel;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class Home extends VerticalLayout implements View {
	
	@Inject
	public Home() {

		setSizeFull();
		
		addStyleName("home");
//
		MenuBar mb = new MenuBar();
		mb.addStyleName("biopama-menu");
		mb.addItem("ABOUT", null);
		mb.addItem("SEARCH", null);
		addComponent(mb);
		mb.setWidth("100%");
		mb.setStyleName(ValoTheme.MENUBAR_BORDERLESS);

//		HtmlLabel c = new HtmlLabel("<div>IBIS</div><div>island biodiversity & invasive species</div>");
		TabSheet c = new TabSheet();


		addComponent(c);
		c.setSizeFull();
		setExpandRatio(c, 1);
		c.addStyleName("content");
		c.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
//		setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		
		c.addTab(getAbout(), "About");
		c.addTab(getThemes(), "Search");
		c.addTab(new HtmlLabel(), "API");
		

		Button c2 = new Button();
		c2.setIcon(FontAwesome.CHEVRON_DOWN);
		addComponent(c2);
		
		setComponentAlignment(c2, Alignment.MIDDLE_CENTER);

	}

	private Component getThemes() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();

		for(Theme t : Theme.values()) {
			hl.addComponent(getPanel(t));
			
		}

		return hl;
	}

	private Panel getPanel(Theme theme) {
		Panel c = new Panel();
		c.setStyleName(ValoTheme.PANEL_BORDERLESS);
		c.setCaption(theme.toString());
		c.setContent(new HtmlLabel("SomeContent"));
		return c;
	}

	private Component getAbout() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();
		Panel c = new Panel();
		c.setStyleName(ValoTheme.PANEL_BORDERLESS);
		hl.addComponent(c);
		c.setContent(new HtmlLabel("Invasive Alien Species (IAS) are recognized as one of the largest drivers of biodiversity loss worldwide (Millenium Ecosystem Assessment 2005).  IAS threaten native species through multiple mechanisms including predation, disease transmission and changing ecosystem function (Simberloff 2008). Island ecosystems are particularly vulnerable to biological invasions (Loope and Mueller-Dombois 1989, Hulme 2004)."));
		return hl;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}
