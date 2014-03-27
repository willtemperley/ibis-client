package org.jrc.server;

import it.jrc.auth.AnonymousAuthServlet;
import it.jrc.auth.AuthFilter;
import it.jrc.auth.JpaRealm;
import it.jrc.auth.SecurityFilter;

import org.apache.shiro.realm.Realm;
import org.vaadin.addons.form.inject.AbstractGuiceServletModule;
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


        bind(Realm.class).to(JpaRealm.class);
        
        /*
         * Persistence objects
         */
//        bind(Dao.class).in(ServletScopes.SESSION);

        /*
         * Bind constants
         */
        Names.bindProperties(binder(), getRuntimeProperties());

        /*
         * Security and persistence filters
         */
        filter("/*").through(PersistFilter.class);
        
        filter("/*").through(SecurityFilter.class, getIni());
        
        filter("/*").through(AuthFilter.class);
        
        
        serve("/login").with(AnonymousAuthServlet.class);
//        serve("/login").with(AuthServlet.class);
//        serve("/login").with(FakeAuthServlet.class);
//        serve("/login").with(OAuthServlet.class);
        
        /*
         * Main application servlet
         */
        serve("/*").with(VGuiceApplicationServlet.class, getServletParams());

    }


}