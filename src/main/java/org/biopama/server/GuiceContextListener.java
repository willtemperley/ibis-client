package org.biopama.server;

import org.biopama.IbisUIBiopama;
import org.biopama.ibis.IbisUI;
import org.biopama.ibis.ViewModule;
import org.vaadin.addons.guice.uiscope.UIScopeModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;

public class GuiceContextListener extends GuiceServletContextListener {
	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new IBISGuiceServletModule(), new ViewModule(), 
				new UIScopeModule(IbisUIBiopama.class), 
				new JpaPersistModule("ibis-domain"));
	}
}
