package org.jrc.server;

import org.issg.ibis.ViewModule;
import org.jrc.inject.MvpModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;

public class GuiceContextListener extends GuiceServletContextListener {
	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new IBISGuiceServletModule(), new ViewModule(), new MvpModule(), new JpaPersistModule("ibis-domain"));
	}
}
