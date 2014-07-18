package org.jrc.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.issg.ibis.webservices.DataServlet;
import org.vaadin.addons.guice.servlet.VGuiceApplicationServlet;
import org.vaadin.addons.oauth.OAuthCredentials;
import org.vaadin.addons.oauth.OAuthManager;
import org.vaadin.addons.oauth.SessionAuthInfo;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.google.inject.persist.PersistFilter;
import com.google.inject.servlet.SessionScoped;

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

        bind(OAuthManager.class).in(SessionScoped.class);
        bind(SessionAuthInfo.class).in(SessionScoped.class);

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

    @Provides
    @Singleton
    public OAuthCredentials getOAuthCredentials() {
    	InputStream stream = this.getClass().getClassLoader()
                    .getResourceAsStream("google.json");
//                    .getResourceAsStream("ibis-client.json");

    	
    	try {
			String content = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
			Gson g = new Gson();

			return g.fromJson(content, OAuthCredentials.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }

}