package org.issg.ibis;

import java.util.Map;

import org.issg.ibis.client.CountryPerspective;
import org.issg.ibis.client.LocationPerspective;
import org.issg.ibis.client.SearchView;
import org.issg.ibis.client.SpeciesPerspective;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.editor.DesignatedAreaEditor;
import org.issg.ibis.editor.IslandEditor;
import org.issg.ibis.editor.SpeciesEditor;
import org.issg.ibis.editor.SpeciesImpactEditor;
import org.jrc.form.view.AbstractViewModule;
import org.jrc.form.view.GuicedViewProvider;
import org.jrc.persist.adminunits.Country;

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


    private static final String SPECIES_PERSPECTIVE = "TaxonName";
    private static final String COUNTRY_PERSPECTIVE = "Country";
    private static final String LOCATION_PERSPECTIVE = "Location";

    @Override
    protected void configure() {
        mapbinder = MapBinder.newMapBinder(binder(), String.class,
                View.class);
        
        mapbinder.addBinding(GuicedViewProvider.HOME).to(SearchView.class);
        mapbinder.addBinding("Search").to(SearchView.class);
        
        mapbinder.addBinding(SPECIES_PERSPECTIVE).to(SpeciesPerspective.class);
        mapbinder.addBinding(COUNTRY_PERSPECTIVE).to(CountryPerspective.class);
        mapbinder.addBinding(LOCATION_PERSPECTIVE).to(LocationPerspective.class);
        
        addBinding("EditSpeciesImpact", SpeciesImpactEditor.class, SpeciesImpact.class);
        addBinding("EditDesignatedArea", DesignatedAreaEditor.class, Location.class);
        addBinding("EditIsland", IslandEditor.class, Location.class);
        addBinding("EditSpecies", SpeciesEditor.class, Species.class);

    }
    
    public static String getSpeciesLink(Species species) {
        
        return SPECIES_PERSPECTIVE + "/" +  species.getId();
        
    }
    
    public static String getCountryLink(Country country) {
        
        return COUNTRY_PERSPECTIVE + "/" +  country.getIsoa3Id();
        
    }
    
}
