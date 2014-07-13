package org.jrc.server;

import org.issg.ibis.webservices.DataServlet;
import org.vaadin.addons.guice.servlet.VGuiceApplicationServlet;

import com.google.inject.name.Names;
import com.google.inject.persist.PersistFilter;

/**
 * 
 * @author Will Temperley
 *
 */
public class IBISGuiceServletModule extends AbstractGuiceServletModule {

    @Override
    protected void configureServlets() {

        /*
         * Bind constants
         */
        Names.bindProperties(binder(), getRuntimeProperties());

        /*
         * Security and persistence filters
         */
        filter("/*").through(PersistFilter.class);
        
        serve("/download/*").with(DataServlet.class);
        
        /*
         * Main application servlet
         */
        serve("/*").with(VGuiceApplicationServlet.class, getServletParams());

    }


}