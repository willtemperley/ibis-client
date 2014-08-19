package org.issg.ibis.qdsl.experimental;

import org.jrc.ui.HtmlLabel;
import com.vaadin.ui.HorizontalLayout;

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
