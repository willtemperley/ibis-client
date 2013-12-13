package org.issg.ibis.a;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.issg.ibis.client.js.SpeciesInfo;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.SpeciesLocation;
import org.issg.ibis.domain.SpeciesLocation_;
import org.issg.ibis.domain.view.SpeciesExtent;
import org.issg.ibis.editor.InlineSpeciesEditor;
import org.jrc.form.editor.EntityTable;
import org.jrc.persist.ContainerManager;
import org.jrc.persist.Dao;
import org.jrc.ui.SimplePanel;
import org.jrc.ui.baseview.TwinPanelView;
import org.vaadin.addon.leaflet.jts.FeatureMapLayer;

import com.google.inject.Inject;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

public class SpeciesMapManager  {

    private Dao dao;
    private Species species;

    private List<Long> locIds = new ArrayList<Long>();
    private FeatureMapLayer fml = new FeatureMapLayer();

    public SpeciesMapManager(Dao dao) {
        this.dao = dao;
    }

    public FeatureMapLayer setSpecies(Species sp) {

        this.species = sp;

        locIds.clear();

        /*
         * Impacts
         */
        Set<SpeciesImpact> speciesImpacts = species.getSpeciesImpacts();

        for (SpeciesImpact speciesImpact : speciesImpacts) {
            Location loc = speciesImpact.getLocation();
            if (locIds.contains(loc.getId())) {
                continue;
            }
            locIds.add(loc.getId());

            fml.addGeometry(loc.getGeom(), "#ff0000");
        }
        
        /*
         * Locations
         */
        EntityManager em = dao.getEntityManager();
        TypedQuery<SpeciesLocation> q = em.createQuery("from SpeciesLocation where species.id = :sid", SpeciesLocation.class);
        q.setParameter("sid", species.getId());
        List<SpeciesLocation> results = q.getResultList();

        for (SpeciesLocation speciesLocation : results) {
            Location location = speciesLocation.getLocation();
            if (locIds.contains(location.getId())) {
                continue;
            }
            Geometry geom = location.getGeom();
            fml.addGeometry(geom, "#00ff00");

        }

        return fml;
//        map.zoomTo(getSpeciesExtent(species.getId()));
    }
    
    
//    private void speciesImpactSelected(SpeciesImpact speciesImpact) {
//        
//        if (speciesImpact == null) return;
//
//        Location location = speciesImpact.getLocation();
//        if (location == null) {
//            System.out.println("Null loc");
//            return;
//        }
//
//        Polygon polyEnv = location.getEnvelope();
//
//        if (polyEnv != null) {
//            map.zoomTo(polyEnv);
//        }
//    }

    /**
     * Gets the species extent.
     * TODO: add this view to the mapping properly
     * 
     * @param id
     * @return
     */
    public Polygon getSpeciesExtent() {
        
        SpeciesExtent extent = dao.find(SpeciesExtent.class, species.getId());
        return extent.getEnvelope();

    }

}
