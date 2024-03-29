package org.biopama.ibis;

import java.util.Map;

import org.biopama.About;
import org.biopama.ibis.editor.LocationEditor;
import org.biopama.ibis.editor.SpeciesEditor;
import org.biopama.ibis.editor.basic.*;
import org.biopama.ibis.perspective.location.LocationPerspective;
import org.biopama.ibis.perspective.shared.UnauthorizedView;
import org.biopama.ibis.perspective.species.SpeciesPerspective;
import org.biopama.ibis.upload.UploadView;
import org.biopama.search.Search;
import org.biopama.server.AbstractViewModule;
import org.biopama.server.GuicedViewProvider;
import org.biopama.Home;
import org.issg.ibis.domain.Country;
import org.issg.ibis.domain.Species;
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

    public static final String ISSUE_EDITOR = "Issues";

//	public static final String SPECIES_SEARCH = "SearchBySpecies";
//	public static final String LOCATION_SEARCH = "SearchByLocation";
//	public static final String SEARCH = "Search";

    public static final String SPECIES_EDITOR = "EditSpecies";
	public static final String LOCATION_EDITOR = "EditLocation";
	public static final String CONSERVATION_CLASSIFICATION = "EditConservationClassification";
	public static final String BIOLOGICAL_STATUS = "EditBiologicalStatus";
	public static final String IMPACT_MECHANISM = "EditImpactMechanism";
	public static final String ORGANISM_TYPE = "EditOrganismType";
    public static final String SITECONTENT_EDITOR = "EditSiteContent";
	public static final String REFERENCE = "EditReference";

	public static final String USER_EDITOR = "UserEditor";
	
	public static final String UPLOAD = "Upload";
	public static final String UNAUTHORIZED = "Unauthorized";
	public static final String SEARCH = "Search";
    public static final String ABOUT = "About";

    @Override
    protected void configure() {
        mapbinder = MapBinder.newMapBinder(binder(), String.class,
                View.class);
        
        mapbinder.addBinding(GuicedViewProvider.HOME).to(Home.class);//.in(UIScoped.class);;
        mapbinder.addBinding(SEARCH).to(Search.class);//.in(UIScoped.class);;
        mapbinder.addBinding(ABOUT).to(About.class);//.in(UIScoped.class);;


        mapbinder.addBinding(UNAUTHORIZED).to(UnauthorizedView.class).in(UIScoped.class);;

        mapbinder.addBinding(SPECIES_PERSPECTIVE).to(SpeciesPerspective.class);
        mapbinder.addBinding(LOCATION_PERSPECTIVE).to(LocationPerspective.class);

        mapbinder.addBinding(LOCATION_EDITOR).to(LocationEditor.class).in(UIScoped.class);
        mapbinder.addBinding(SPECIES_EDITOR).to(SpeciesEditor.class).in(UIScoped.class);
        mapbinder.addBinding(CONSERVATION_CLASSIFICATION).to(ConservationClassificationEditor.class).in(UIScoped.class);

        mapbinder.addBinding(BIOLOGICAL_STATUS).to(BiologicalStatusEditor.class).in(UIScoped.class);
        mapbinder.addBinding(IMPACT_MECHANISM).to(ImpactMechanismEditor.class).in(UIScoped.class);
        mapbinder.addBinding(ORGANISM_TYPE).to(OrganismTypeEditor.class).in(UIScoped.class);
        mapbinder.addBinding(REFERENCE).to(ReferenceEditor.class).in(UIScoped.class);
        mapbinder.addBinding(SITECONTENT_EDITOR).to(SiteContentEditor.class).in(UIScoped.class);

        mapbinder.addBinding(USER_EDITOR).to(UserEditor.class).in(UIScoped.class);
        mapbinder.addBinding(UPLOAD).to(UploadView.class).in(UIScoped.class);

    }
    
    public static String getSpeciesLink(Species species) {
        
        return SPECIES_PERSPECTIVE + "/" +  species.getId();
        
    }
    
    public static String getCountryLink(Country country) {
        
        return COUNTRY_PERSPECTIVE + "/" +  country.getIsoa3Id();
        
    }
    
}
