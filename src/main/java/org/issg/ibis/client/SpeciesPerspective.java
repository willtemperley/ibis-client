package org.issg.ibis.client;

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

public class SpeciesPerspective extends TwinPanelView implements View {

    private Dao dao;
    private Species species;
    private BeanItemContainer<SpeciesImpact> bic = new BeanItemContainer<SpeciesImpact>(
            SpeciesImpact.class);
    private SpeciesSummaryView speciesSummary;
    private InlineSpeciesEditor speciesEditor;
    private LayerViewer map;
    
    private List<Long> locIds = new ArrayList<Long>();
    
    private FeatureMapLayer fml = new FeatureMapLayer();

    private JPAContainer<SpeciesLocation> speciesLocationContainer;

    /**
     * Displays a threatened species and its locations.
     * 
     * @param dao
     * @param speciesSelector
     */
    @Inject
    public SpeciesPerspective(Dao dao) {

        this.dao = dao;
        setExpandRatios(1f, 1.5f);

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
            EntityTable<SpeciesImpact> table = getSpeciesImpactTable();
            /*
             * Species info
             */
            SimplePanel leftPanel = getLeftPanel();
            this.speciesSummary = new SpeciesSummaryView(leftPanel, table);
            
            //TODO - edit for people
//            addEditButton(leftPanel);

        }

        {
            SimplePanel rightPanel = getRightPanel();

            VerticalLayout vl = new VerticalLayout();
            vl.setSizeFull();
            
            /*
             * Map
             */
            map = new LayerViewer();
            map.getMap().addComponent(fml);
            map.setSizeFull();
            
            vl.addComponent(map);
            /*
             * Table
             */
            rightPanel.addComponent(vl);
        }
    }

    private EntityTable<SpeciesLocation> getSpeciesLocationTable() {

        ContainerManager<SpeciesLocation> locationContainerManager = new ContainerManager<SpeciesLocation>(
                dao, SpeciesLocation.class);

        this.speciesLocationContainer = locationContainerManager.getContainer();

        EntityTable<SpeciesLocation> table = new EntityTable<SpeciesLocation>(
                speciesLocationContainer);

        table.setHeight("300px");
        table.setPageLength(20);

        table.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {

            }
        });

        table.addColumns(SpeciesLocation_.species,
                SpeciesLocation_.biologicalStatus);
        return table;
    }

    private void addEditButton(SimplePanel leftPanel) {
        Button editButton = leftPanel.addActionButton(SimplePanel.ButtonIcon.icon_cog); 
        editButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                showWindow();
            }
        });
    }

    protected void refresh() {
        
        species = dao.find(Species.class, species.getId());
        speciesSummary.setSpecies(species);
        
    }

    protected void showWindow() {
        speciesEditor.edit(dao.find(Species.class, species.getId()));

    }

//    private EntityTable<SpeciesImpact> getSpeciesImpactTable() {
//
//        EntityTable<SpeciesImpact> table = new EntityTable<SpeciesImpact>(bic);
//
//        table.setHeight("300px");
//        table.setPageLength(20);
//
//        table.addValueChangeListener(new Property.ValueChangeListener() {
//            public void valueChange(Property.ValueChangeEvent event) {
//
//                SpeciesImpact si = (SpeciesImpact) event.getProperty()
//                        .getValue();
//
//                speciesImpactSelected(si);
//            }
//        });
//
//        table.addColumns(SpeciesImpact_.invasiveSpecies,
//                SpeciesImpact_.impactMechanism,
//                SpeciesImpact_.impactOutcome);
//        return table;
//    }

    private EntityTable<SpeciesImpact> getSpeciesImpactTable() {
        
        EntityTable<SpeciesImpact> table = new EntityTable<SpeciesImpact>(bic);
        table.setColumnHeaderMode(com.vaadin.ui.Table.ColumnHeaderMode.HIDDEN);

        table.setHeight("200px");
        table.setWidth("500px");
        table.setPageLength(20);

        table.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {

                SpeciesImpact si =  (SpeciesImpact) event.getProperty().getValue();
                speciesImpactSelected(si);

            }
        });

		ImpactVisualizationColumn2 generatedColumn = new ImpactVisualizationColumn2();
        table.addGeneratedColumn("id", generatedColumn);
		table.setColumnWidth("id", 400);
		table.setVisibleColumns("id");
        return table;
        
    }

    @Override
    public void enter(ViewChangeEvent event) {

        String params = event.getParameters();

//        setEntityId(params);
        setSpeciesId(params);
    }

    private void setSpeciesId(String params) {
        Long id = Long.valueOf(params);

        Species sp = dao.find(Species.class, id);
//    }
//
//    private void setEntityId(String id) {
//
//        id = id.replace("-", " ");
//        Species sp = dao.findByProxyId(Species_.name, id);

        this.species = sp;

        if (sp == null) {
            Notification.show("Could not find species: " + id);
            return;
        }

        /*
         * Species display
         */
        speciesSummary.setSpecies(sp);

        //Clear other info
        bic.removeAllItems();
        locIds.clear();

        /*
         * Impacts
         */
        Set<SpeciesImpact> speciesImpacts = species.getSpeciesImpacts();

        for (SpeciesImpact speciesImpact : speciesImpacts) {
            bic.addBean(speciesImpact);
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

        map.zoomTo(getSpeciesExtent(species.getId()));
    }
    
    
    private void speciesImpactSelected(SpeciesImpact speciesImpact) {
        
        if (speciesImpact == null) return;

        Location location = speciesImpact.getLocation();
        if (location == null) {
            System.out.println("Null loc");
            return;
        }

        Polygon polyEnv = location.getEnvelope();

        if (polyEnv != null) {
            map.zoomTo(polyEnv);
        }
    }

    /**
     * Gets the species extent.
     * TODO: add this view to the mapping properly
     * 
     * @param id
     * @return
     */
    private Polygon getSpeciesExtent(Long id) {
        
        SpeciesExtent extent = dao.find(SpeciesExtent.class, id);
    
        return extent.getEnvelope();

    }

}
