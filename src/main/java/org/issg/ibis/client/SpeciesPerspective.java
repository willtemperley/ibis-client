package org.issg.ibis.client;

import org.issg.ibis.client.js.LeafletMap;
import org.issg.ibis.client.js.SpeciesInfo;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.SpeciesImpact_;
import org.issg.ibis.domain.Species_;
import org.jrc.form.editor.EditorPanel;
import org.jrc.form.editor.EntityTable;
import org.jrc.persist.ContainerManager;
import org.jrc.persist.Dao;

import com.google.inject.Inject;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Polygon;

public class SpeciesPerspective extends HorizontalLayout implements View {

    private Dao dao;
    private Species species;
    private LeafletMap leafletMap;
    private BeanItemContainer<SpeciesImpact> bic = new BeanItemContainer<SpeciesImpact>(
            SpeciesImpact.class);
    private FlickrTest flickr;
    private SpeciesSummary speciesSummary;

    @Inject
    public SpeciesPerspective(Dao dao, SpeciesInfo speciesInfo,
            SpeciesSelector speciesSelector, FlickrTest flickr) {

        this.dao = dao;
        this.flickr = flickr;
        
        setSizeFull();
        setSpacing(true);

        {

            EditorPanel ep = new EditorPanel();
//            ep.setHeight("800px");

            /*
             * Species info
             */
            this.speciesSummary = new SpeciesSummary();
            ep.addComponent(speciesSummary);

            /*
             * Species selector
             */
            ep.addComponent(speciesSelector);
            addComponent(ep);
        }

        {

            EditorPanel ep = new EditorPanel();
            
            VerticalLayout vl = new VerticalLayout();
            /*
             * Map
             */
            leafletMap = new LeafletMap();
            leafletMap.addStyleName("species-map");

            /*
             * Table
             */
            
            EntityTable<SpeciesImpact> table = getTable();
            table.addColumns(SpeciesImpact_.threatenedSpecies,
                    SpeciesImpact_.invasiveSpecies, SpeciesImpact_.location,
                    SpeciesImpact_.impactMechanism,
                    SpeciesImpact_.impactOutcome);

            vl.addComponent(leafletMap);
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
                leafletMap.zoomTo(env);
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
        setSpecies(sp);
    }

    private void setSpecies(Species sp) {
        this.species = sp;
        String imgUrl = flickr.getSingleImageUrl(species.getGenus().getLabel());
        speciesSummary.setSpecies(sp, imgUrl);

        bic.removeAllItems();
        bic.addAll(species.getSpeciesImpacts());

    }

}
