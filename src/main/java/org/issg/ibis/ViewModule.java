package org.issg.ibis;

import java.util.Map;

import org.issg.ibis.client.SpeciesPerspective;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.editor.SpeciesEditor;
import org.issg.ibis.editor.SpeciesImpactEditor;
import org.jrc.form.view.AbstractViewModule;
import org.jrc.form.view.GuicedViewProvider;

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


    private static final String SPECIES_PERSPECTIVE = "Species-Perspective";

    @Override
    protected void configure() {
        mapbinder = MapBinder.newMapBinder(binder(), String.class,
                View.class);
        
        mapbinder.addBinding(GuicedViewProvider.HOME).to(IndexPage.class);
        
        mapbinder.addBinding(SPECIES_PERSPECTIVE).to(SpeciesPerspective.class);
        
        addBinding("Species-Impact", SpeciesImpactEditor.class, SpeciesImpact.class);
        addBinding("Species", SpeciesEditor.class, Species.class);

    }
    
    public static String getSpeciesLink(Species species) {
        
        return SPECIES_PERSPECTIVE + "/" +  species.getName().replace(" ", "-");
        
    }
    
    
}
