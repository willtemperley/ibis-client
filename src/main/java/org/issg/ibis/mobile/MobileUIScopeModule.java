package org.issg.ibis.mobile;

import org.vaadin.addons.guice.uiscope.UIScopeModule;
import org.vaadin.addons.guice.uiscope.UIScoped;


public class MobileUIScopeModule extends UIScopeModule{

	public MobileUIScopeModule() {
		super(SimplePhoneUI.class);
	}
	
	@Override
	public void configure() {
		bind(GuicedNavigationManager.class).in(UIScoped.class);
		super.configure();
	}
}
