package org.issg.ibis.mobile;
import org.jrc.edit.Dao;
import org.vaadin.addons.guice.ui.ScopedUI;

import com.google.inject.Inject;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;

@Theme("mobiletheme")
@Widgetset("org.issg.ibis.M_AppWidgetset")
public class SimplePhoneUI extends ScopedUI {
	
	
	private GuicedNavigationManager navMan;

	@Inject
	public SimplePhoneUI(Dao dao, GuicedNavigationManager navMan) {
		this.navMan = navMan;
	}

    @Override
    protected void init(VaadinRequest request) {
        // Use it as the content root
    	
    	setContent(new MainTabsheet());

//    	navMan.navigateTo(LocationView.class);
    }
}