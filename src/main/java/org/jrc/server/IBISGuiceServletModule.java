package org.jrc.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.webservices.DataServlet;
import org.vaadin.addons.guice.servlet.VGuiceApplicationServlet;
import org.vaadin.addons.oauth.OAuthCredentials;
import org.vaadin.addons.oauth.OAuthManager;
import org.vaadin.addons.oauth.OAuthSubject;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gwt.dev.util.collect.HashMap;
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

		bind(OAuthSubject.class).to(RoleManager.class).in(SessionScoped.class);
		bind(OAuthManager.class).in(SessionScoped.class);

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
	public Map<String, OAuthCredentials> getOAuthCredentials() throws IOException {

		Map<String, OAuthCredentials> x = new HashMap<String, OAuthCredentials>();
		Gson g = new Gson();
		String[] cred = new String[] { "google", "linkedin" };
		for (int i = 0; i < cred.length; i++) {
			InputStream stream = this.getClass().getClassLoader()
					.getResourceAsStream(cred[i] + ".json");
			String content = CharStreams.toString(new InputStreamReader(stream,
					Charsets.UTF_8));
			 OAuthCredentials oac = g.fromJson(content, OAuthCredentials.class);

			 x.put(cred[i], oac);

		}
		return x;
	}

}