package org.issg.ibis.a;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.issg.ibis.client.ImpactVisualizationColumn2;
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
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;


public class SpeciesPerspective extends Panel implements View {

    private Dao dao;
    private Species species;
    private BeanItemContainer<SpeciesImpact> bic = new BeanItemContainer<SpeciesImpact>(
            SpeciesImpact.class);
    private SpeciesSummaryView speciesSummary;
    private InlineSpeciesEditor speciesEditor;
    

    private JPAContainer<SpeciesLocation> speciesLocationContainer;
    private IbisMap ibisMap;

    /**
     * Displays a threatened species and its locations.
     * 
     * @param dao
     * @param speciesSelector
     */
    @Inject
    public SpeciesPerspective(Dao dao, IbisMap ibisMap) {

        this.dao = dao;
        this.ibisMap = ibisMap;

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
            this.speciesSummary = new SpeciesSummaryView();
            
            //TODO - edit for people
//            addEditButton(leftPanel);
            setContent(speciesSummary);

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

        setSpeciesId(params);
    }

    private void setSpeciesId(String params) {
        Long id = Long.valueOf(params);

        Species sp = dao.find(Species.class, id);
//

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


        ibisMap.setSpecies(species);
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
            ibisMap.zoomTo(polyEnv);
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
