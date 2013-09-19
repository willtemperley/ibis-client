package org.issg.ibis.client;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.issg.ibis.client.js.LeafletMap;
import org.issg.ibis.client.js.SpeciesInfo;
import org.issg.ibis.display.SimpleHtmlHeader;
import org.issg.ibis.display.SimplePanel;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.SpeciesImpact_;
import org.issg.ibis.domain.Species_;
import org.issg.ibis.domain.view.SpeciesExtent;
import org.issg.ibis.domain.view.ThreatenedSpeciesLocation;
import org.issg.ibis.editor.InlineSpeciesEditor;
import org.jrc.form.editor.EditorPanel;
import org.jrc.form.editor.EntityTable;
import org.jrc.persist.Dao;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.inject.Inject;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class SpeciesPerspective extends HorizontalLayout implements View {

    private Dao dao;
    private Species species;
    private LeafletMap map;
    private BeanItemContainer<SpeciesImpact> bic = new BeanItemContainer<SpeciesImpact>(
            SpeciesImpact.class);
    private SpeciesSummary speciesSummary;
    private InlineSpeciesEditor speciesEditor;

    // private OpenLayersMap map;

    @Inject
    public SpeciesPerspective(Dao dao, SpeciesInfo speciesInfo,
            SpeciesSelector speciesSelector) {

        this.dao = dao;

        setSizeFull();
        setSpacing(true);

        {
            this.speciesEditor = new InlineSpeciesEditor(Species.class, dao);
            speciesEditor.getWindow().addCloseListener(
                    new Window.CloseListener() {
                        @Override
                        public void windowClose(CloseEvent e) {
                            
                            refresh();
                        }
                    });
        }

        {
            /*
             * Species info
             */
            this.speciesSummary = new SpeciesSummary();
            Button editButton = speciesSummary.addActionButton(SimplePanel.ButtonIcon.icon_cog); // FIXME quick
                                                                  // test
            editButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    showWindow();
                }
            });

            speciesSummary.setHeight("100%");

            EntityTable<SpeciesImpact> table = getSpeciesImpactTable();

            speciesSummary.addComponent(new SimpleHtmlHeader("Impacts"));
            speciesSummary.addComponent(table);

            addComponent(speciesSummary);
            setExpandRatio(speciesSummary, 1);

        }

        {

            SimplePanel ep = new SimplePanel();
            ep.setSizeFull();

            VerticalLayout vl = new VerticalLayout();
            vl.setSizeFull();
            /*
             * Map
             */
            map = new LeafletMap();
            map.setSizeFull();
            map.addStyleName("species-map");

            vl.addComponent(map);
            /*
             * Table
             */
            ep.addComponent(vl);
            addComponent(ep);
            setExpandRatio(ep, 1.5f);

        }
    }

    protected void refresh() {
        
        species = dao.find(Species.class, species.getId());
        speciesSummary.setSpecies(species);
        
    }

    protected void showWindow() {
        speciesEditor.edit(dao.find(Species.class, species.getId()));

    }

    private EntityTable<SpeciesImpact> getSpeciesImpactTable() {

        EntityTable<SpeciesImpact> table = new EntityTable<SpeciesImpact>(bic);

        table.setHeight("300px");
        table.setPageLength(20);

        table.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {

                SpeciesImpact si = (SpeciesImpact) event.getProperty()
                        .getValue();

                speciesImpactSelected(si);
            }
        });

        table.addColumns(SpeciesImpact_.invasiveSpecies,
                SpeciesImpact_.location, SpeciesImpact_.impactMechanism,
                SpeciesImpact_.impactOutcome);
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
            Notification.show("Could not find species: " + id);
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

        TypedQuery<String> q = dao
                .getEntityManager()
                .createQuery(
                        "select geoJson from ThreatenedSpeciesLocation where threatenedSpeciesId = ?1",
                        String.class);
        q.setParameter(1, species.getId());

        List<String> res = q.getResultList();

        map.addPolygons(res);

        /*
         * Species extent
         */
        Polygon env = getSpeciesExtent(species.getId());
        if (env != null) {
            map.zoomTo(env.getEnvelopeInternal());
        }

    }

    private void speciesImpactSelected(SpeciesImpact speciesImpact) {

        if (speciesImpact == null) {
            return;
        }
        Location loc = speciesImpact.getLocation();
        if (loc == null) {
            return;
        }

        Polygon polyEnv = loc.getEnvelope();
        Envelope env = polyEnv.getEnvelopeInternal();

        // FIXME
        map.zoomTo(env);
    }

    /**
     * Redlist stuff; ignore for now.
     * 
     * @param id
     * @return
     */
    private Polygon getSpeciesExtent(Long id) {

        EntityManager em = dao.getEntityManager();
        TypedQuery<SpeciesExtent> q = em.createQuery(
                "from SpeciesExtent where id = :id", SpeciesExtent.class);
        q.setParameter("id", id);

        List<SpeciesExtent> res = q.getResultList();
        if (res.size() == 1) {
            return res.get(0).getEnvelope();
        }
        return null;
    }

    /**
     * Redlist stuff; ignore for now.
     * 
     * @return
     */
    private Integer getBinomialId() {
        Integer binomialId = dao
                .scalarNativeQuery(
                        "select id from species_distribution.binomial where binomial = ?1",
                        species.getName());
        return binomialId;
    }

}
