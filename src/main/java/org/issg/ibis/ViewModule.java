package org.issg.ibis;

import java.util.Map;

import org.issg.ibis.domain.Country;
import org.issg.ibis.domain.Species;
import org.issg.ibis.editor.LocationEditor;
import org.issg.ibis.perspective.location.LocationPerspective;
import org.issg.ibis.perspective.species.SpeciesPerspective;
import org.issg.ibis.responsive.Dashboard;
import org.issg.ibis.responsive.archive.Dash2;
import org.jrc.server.AbstractViewModule;
import org.jrc.server.GuicedViewProvider;
import org.vaadin.addons.guice.uiscope.UIScoped;

import com.google.inject.multibindings.MapBinder;
import com.vaadin.navigator.View;

/**
 * Binds views to their URLs which results in an injectable {@link Map} of
 * views.
 * 
 * Editors will appear in navigation under the first part of the URL, named as
 * the second part of the URL.
 * 
 * Also provides reverse links through a class-to-url mapping
 * 
 * @author will
 */
public class ViewModule extends AbstractViewModule {


    public static final String SPECIES_PERSPECTIVE = "Species";
    public static final String LOCATION_PERSPECTIVE = "Location";

    public static final String COUNTRY_PERSPECTIVE = "Country";

    public static final String SPECIES_EDITOR = "EditSpecies";
    public static final String ISSUE_EDITOR = "Issues";
	public static final String SPECIES_SEARCH = "SearchBySpecies";
	public static final String LOCATION_SEARCH = "SearchByLocation";
	public static final String SEARCH = "Search";
	public static final String LOCATION_EDITOR = "EditLocation";

    @Override
    protected void configure() {
        mapbinder = MapBinder.newMapBinder(binder(), String.class,
                View.class);
        
        mapbinder.addBinding(GuicedViewProvider.HOME).to(Dashboard.class);//.in(UIScoped.class);;
        mapbinder.addBinding("OLD"+GuicedViewProvider.HOME).to(Dash2.class).in(UIScoped.class);;
        
        mapbinder.addBinding(SPECIES_PERSPECTIVE).to(SpeciesPerspective.class).in(UIScoped.class);
        mapbinder.addBinding(LOCATION_PERSPECTIVE).to(LocationPerspective.class).in(UIScoped.class);

//        mapbinder.addBinding(OAUTH_VIEW).to(AuthView.class).in(UIScoped.class);

//        mapbinder.addBinding(SPECIES_SEARCH).to(SpeciesSearch.class).in(UIScoped.class);
//        mapbinder.addBinding(LOCATION_SEARCH).to(LocationSearch.class).in(UIScoped.class);

//        mapbinder.addBinding(SEARCH).to(SimpleSearch.class).in(UIScoped.class);
//        mapbinder.addBinding("Mobile").to(MyFirstMobileUI.class).in(UIScoped.class);

        
//        addBinding(SPECIES_EDITOR, SpeciesEditor.class, Species.class);

//        addBinding("EditSpeciesImpact", SpeciesImpactEditor.class, SpeciesImpact.class);
        mapbinder.addBinding(LOCATION_EDITOR).to(LocationEditor.class).in(UIScoped.class);

    }
    
    public static String getSpeciesLink(Species species) {
        
        return SPECIES_PERSPECTIVE + "/" +  species.getId();
        
    }
    
    public static String getCountryLink(Country country) {
        
        return COUNTRY_PERSPECTIVE + "/" +  country.getIsoa3Id();
        
    }
    
}
