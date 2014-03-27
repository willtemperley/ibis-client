package org.jrc.server;

import org.issg.ibis.AppUI;
import org.issg.ibis.ViewModule;
import org.issg.ibis.client.deprecated.IbisUI;
import org.vaadin.addons.guice.uiscope.UIScopeModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;

public class GuiceContextListener extends GuiceServletContextListener {
	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new IBISGuiceServletModule(), new ViewModule(), new UIScopeModule(AppUI.class), new JpaPersistModule("ibis-domain"));
	}
}
