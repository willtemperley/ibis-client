package org.issg.ibis.client.deprecated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.SpeciesLocation;
import org.issg.ibis.domain.view.SpeciesExtent;
import org.issg.ibis.perspective.shared.LayerViewer;
import org.jrc.persist.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.jts.FeatureMapLayer;
import org.vaadin.addons.guice.uiscope.UIScoped;

import com.google.inject.Inject;
import com.vaadin.ui.Component;

/**
 * The map, used by all perspectives
 * 
 * @author Will Temperley
 *
 */

@UIScoped
public class IbisMap extends LayerViewer {

    private FeatureMapLayer fml = new FeatureMapLayer();
    private Dao dao;
    
    private Map<Long, Set<LPolygon>> locs = new HashMap<Long, Set<LPolygon>>();
    
    private Logger logger = LoggerFactory.getLogger(IbisMap.class);
    
    @Inject
    public IbisMap(Dao dao) {

        getMap().addComponent(fml);
        this.dao = dao;

    }

    public void setSpecies(Species species) {
    
            hideVisibleLocations();
            
            /*
             * Locations
             */
            EntityManager em = dao.getEntityManager();
            TypedQuery<SpeciesLocation> q = em.createQuery("from SpeciesLocation where species.id = :sid", SpeciesLocation.class);
            q.setParameter("sid", species.getId());
            List<SpeciesLocation> results = q.getResultList();
    
            for (SpeciesLocation speciesLocation : results) {
                Location loc = speciesLocation.getLocation();
                addOrGetPolys(loc, "#00ff00");
                logger.debug("Occurrence at loc id: " + loc.getId());
            }

            /*
             * Impacts
             */
            Set<SpeciesImpact> speciesImpacts = species.getSpeciesImpacts();
    
            for (SpeciesImpact speciesImpact : speciesImpacts) {
                Location loc = speciesImpact.getLocation();
                addOrGetPolys(loc, "#ff0000");
                logger.debug("Impact at loc id: " + loc.getId());
            }
            
            SpeciesExtent extent = dao.find(SpeciesExtent.class, species.getId());
            if (extent != null) {
                this.zoomTo(extent.getEnvelope());
            } else {
                logger.debug("Null species extent");
            }
        }

    private void hideVisibleLocations() {
        for (Component component : fml) {
            if (component.isVisible()) {
                component.setVisible(false);
            }
        }
    }


    private void addOrGetPolys(Location loc, String colorString) {
        Set<LPolygon> polys;
        if (locs.containsKey(loc.getId())) {
           polys = locs.get(loc.getId());
           for (LPolygon lPolygon : polys) {
               lPolygon.setColor(colorString);
               lPolygon.setVisible(true);
           }
        } else {
//           polys = fml.addGeometry(loc.getGeom(), colorString);
//           locs.put(loc.getId(), polys);
        }
    }

    public void setLocation(Location loc) {
        hideVisibleLocations();
        addOrGetPolys(loc, "#0000ff");
        this.zoomTo(loc.getEnvelope());
    }
}
