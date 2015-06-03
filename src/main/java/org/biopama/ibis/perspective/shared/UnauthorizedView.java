package org.biopama.ibis.perspective.shared;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

public class UnauthorizedView extends VerticalLayout implements View{

	@Override
	public void enter(ViewChangeEvent event) {
		
		Notification.show("Unauthorized access.");
		
	}

}
