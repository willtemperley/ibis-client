package org.issg.ibis.qdsl.search;

import org.jrc.ui.HtmlLabel;
import org.jrc.ui.SimplePanel;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class Logos extends HorizontalLayout {
	
	public Logos() {
		addStyleName("logo-container");
		
		addLogo("issg-logo");
		addLogo("ec-logo");
		addLogo("iucn-logo");
		
	}

    private void addLogo(String className) {
        HtmlLabel logoLabel = new HtmlLabel();
        logoLabel.setValue("<div class='" + className + "'></div>");

        addComponent(logoLabel);
    }
}
