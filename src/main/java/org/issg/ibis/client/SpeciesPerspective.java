package org.issg.ibis.client;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.issg.ibis.client.js.LeafletMap;
import org.issg.ibis.client.js.SpeciesInfo;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesExtent;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.SpeciesImpact_;
import org.issg.ibis.domain.Species_;
import org.jrc.form.editor.EditorPanel;
import org.jrc.form.editor.EntityTable;
import org.jrc.persist.Dao;

import com.google.inject.Inject;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Polygon;

public class SpeciesPerspective extends HorizontalLayout implements View {

    private Dao dao;
    private Species species;
    private LeafletMap map;
    private BeanItemContainer<SpeciesImpact> bic = new BeanItemContainer<SpeciesImpact>(
            SpeciesImpact.class);
    private SpeciesSummary speciesSummary;
//    private OpenLayersMap map;

    @Inject
    public SpeciesPerspective(Dao dao, SpeciesInfo speciesInfo,
            SpeciesSelector speciesSelector) {

        this.dao = dao;
        
        setSizeFull();
        setSpacing(true);

        {
            VerticalLayout vl = new VerticalLayout();
            addComponent(vl);

            /*
             * Species info
             */
            this.speciesSummary = new SpeciesSummary();
            vl.addComponent(speciesSummary);

            /*
             * Species selector
             */
            vl.addComponent(speciesSelector);
            addComponent(vl);
        }

        {

            EditorPanel ep = new EditorPanel();
            ep.setSizeFull();
            
            VerticalLayout vl = new VerticalLayout();
            /*
             * Map
             */
            map = new LeafletMap();
            map.setWidth("100%");
            map.addStyleName("species-map");

            /*
             * Table
             */
            EntityTable<SpeciesImpact> table = getTable();
            table.addColumns(SpeciesImpact_.threatenedSpecies,
                    SpeciesImpact_.invasiveSpecies, SpeciesImpact_.location,
                    SpeciesImpact_.impactMechanism,
                    SpeciesImpact_.impactOutcome);

            vl.addComponent(map);
            vl.addComponent(table);
            ep.addComponent(vl);
            addComponent(ep);

        }
    }

    private EntityTable<SpeciesImpact> getTable() {

        EntityTable<SpeciesImpact> table = new EntityTable<SpeciesImpact>(bic);

        table.setHeight("100%");
        table.setPageLength(20);

        table.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {

                SpeciesImpact si = (SpeciesImpact) event.getProperty()
                        .getValue();

                if (si == null) {
                    return;
                }

                Location loc = si.getLocation();

                if (loc == null) {
                    return;
                }

                Polygon poly = loc.getEnvelope();
                Envelope env = poly.getEnvelopeInternal();
                
//                map.setSpecies(getBinomialId());
                
                map.zoomTo(env);
                
            }
        });

        return table;
    }

    @Override
    public void enter(ViewChangeEvent event) {

        String params = event.getParameters();

        setEntityId(params);
    }

    private void setEntityId(String id) {

        id = id.replace("-", " ");
        Species sp = dao.findByProxyId(Species_.name, id);
        
        this.species = sp;
        
        if (sp == null) {
            Notification.show("Null species");
            return;
        }
        
        /*
         * Species display
         */
        speciesSummary.setSpecies(sp);
        
        /*
         * Impacts
         */
        bic.removeAllItems();
        bic.addAll(species.getSpeciesImpacts());
        
        /*
         * Zoom
         */
        Integer binomialId = getBinomialId();
//        if (binomialId != null) {
//            map.setSpecies(binomialId.toString());
//            Notification.show("binomial " + binomialId);
//        } 
        
        Polygon extent = getSpeciesExtent(binomialId);
        map.zoomTo(extent.getEnvelopeInternal(), binomialId.toString());

    }

    private Integer getBinomialId() {
        Integer binomialId = dao.scalarNativeQuery("select id from species_distribution.binomial where binomial = ?1", species.getName());
        return binomialId;
    }
    
    private Polygon getSpeciesExtent(Integer id) {
        
        EntityManager em = dao.getEntityManager();
        TypedQuery<SpeciesExtent> q = em.createQuery("from SpeciesExtent where id = :id", SpeciesExtent.class);
        q.setParameter("id", id.longValue());
        SpeciesExtent se = q.getSingleResult();
        return se.getEnvelope();
    }

}
