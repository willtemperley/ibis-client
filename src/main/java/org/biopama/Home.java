package org.biopama;

import com.vaadin.shared.ui.label.ContentMode;
import org.jrc.ui.HtmlHeader;
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
		setMargin(false);

//		HtmlLabel c = new HtmlLabel("<div>IBIS</div><div>island biodiversity & invasive species</div>");
		String text = "<div>IBIS</div><div class='ibis-text'><span class='bio-text'>island biodiversity</span>&nbsp;<span class='inv-text'>invasive species</span></div>";
		HtmlLabel l = new HtmlLabel(text);
		l.setSizeFull();
		l.addStyleName("big-home-logo");
		addComponent(l);

		HtmlLabel descr = new HtmlLabel();
		descr.setValue("IBIS aims to record and provide information on the occurrence, biological status and impacts of invasive alien species on native species on islands (with a focus on those that are classified as ‘threatened’ in the IUCN Red List of Threatened Species- Critically Endangered (CR), Endangered (EN) and Vulnerable (VU)), and threatened species on National Red Lists) and the prevention and management of this threat.IBIS aims to record and provide information on the occurrence, biological status and impacts of invasive alien species on native species on islands (with a focus on those that are classified as ‘threatened’ in the IUCN Red List of Threatened Species- Critically Endangered (CR), Endangered (EN) and Vulnerable (VU)), and threatened species on National Red Lists) and the prevention and management of this threat.");
		addComponent(descr);
		descr.addStyleName("home-middle");

		Label logoLabel = new Label();
		logoLabel.setContentMode(ContentMode.HTML);
		logoLabel.setValue("<div class='logo-container'><div class='issg-logo'></div><div class='ec-logo'></div><div class='iucn-logo'></div></div>");

		addComponent(logoLabel);

	}

	private Component getThemes() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();

		String[] arr = new String[3];
		arr[0] = "native";
		arr[1] = "invasive";
		arr[2] = "location";
		for (int i = 0; i < arr.length; i++) {

			VerticalLayout vl = new VerticalLayout();
			vl.addComponent(new HtmlHeader(arr[i]));
			vl.addStyleName(arr[i] + "-species");
			vl.addStyleName("theme-box");
			vl.addComponent(new HtmlLabel("Some text"));
			hl.addComponent(vl);
		}

		return hl;
	}


	private Component getAbout() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();
		Panel c = new Panel();
		c.setStyleName(ValoTheme.PANEL_BORDERLESS);
		hl.addComponent(c);
		HtmlLabel content = new HtmlLabel("Invasive Alien Species (IAS) are recognized as one of the largest drivers of biodiversity loss worldwide (Millenium Ecosystem Assessment 2005).  IAS threaten native species through multiple mechanisms including predation, disease transmission and changing ecosystem function (Simberloff 2008). Island ecosystems are particularly vulnerable to biological invasions (Loope and Mueller-Dombois 1989, Hulme 2004).");
		c.setContent(content);

		return hl;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}
