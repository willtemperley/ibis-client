package org.issg.ibis.client;

import java.util.Collection;

import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.SpeciesImpact_;
import org.issg.ibis.domain.SpeciesLocation;
import org.issg.ibis.domain.SpeciesLocation_;
import org.jrc.form.editor.EntityTable;
import org.jrc.persist.ContainerManager;
import org.jrc.persist.Dao;
import org.jrc.persist.adminunits.Country;
import org.jrc.ui.SimpleHtmlHeader;
import org.jrc.ui.SimpleHtmlLabel;
import org.jrc.ui.SimplePanel;
import org.jrc.ui.baseview.TwinPanelView;
import org.vaadin.addon.leaflet.LFeatureGroup;
import org.vaadin.addon.leaflet.LeafletLayer;
import org.vaadin.addon.leaflet.util.JTSUtil;

import com.google.inject.Inject;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vividsolutions.jts.geom.Polygon;

public class LocationPerspective extends TwinPanelView implements View {

    private Dao dao;
    private Country country;
    private LayerViewer map;
    private BeanItemContainer<SpeciesLocation> bic = new BeanItemContainer<SpeciesLocation>(
            SpeciesLocation.class);
    private SimpleHtmlHeader locationName;
    private JPAContainer<SpeciesImpact> speciesImpactContainer;
    private JPAContainer<SpeciesLocation> speciesLocationContainer;
    private SimpleHtmlLabel locationDescription = new SimpleHtmlLabel();
    private LocationSummaryView ls;

    /**
     */
    @Inject
    public LocationPerspective(Dao dao) {

        this.dao = dao;

        getLeftPanel().setWidth("600px");
        setExpandRatio(getRightPanel(), 1);

        {
            /*
             * Location info
             */
            SimplePanel leftPanel = getLeftPanel();
            locationName = new SimpleHtmlHeader();
            locationName.addStyleName("header-large");
            leftPanel.addComponent(locationName);
            leftPanel.addComponent(locationDescription);

            {

                SimpleHtmlHeader impacts = new SimpleHtmlHeader("Known impacts");
                leftPanel.addComponent(impacts);

                ContainerManager<SpeciesImpact> containerManager = new ContainerManager<SpeciesImpact>(
                        dao, SpeciesImpact.class);

                this.speciesImpactContainer = containerManager.getContainer();

                leftPanel.addComponent(getSpeciesImpactTable());

                SimpleHtmlHeader spacer = new SimpleHtmlHeader("");
                leftPanel.addComponent(spacer);

                ls = new LocationSummaryView(leftPanel);

            }

            {
                // SimpleHtmlHeader impacts = new SimpleHtmlHeader("Impacts");
                // leftPanel.addComponent(impacts);
                //
                // leftPanel.addComponent(getSpeciesImpactTable());
            }

        }

        {
            SimplePanel rightPanel = getRightPanel();

            VerticalLayout vl = new VerticalLayout();
            vl.setSizeFull();
            rightPanel.addComponent(vl);

            /*
             * Map
             */
            map = new LayerViewer();
            map.setSizeFull();

            vl.addComponent(map);

            /*
             * Table
             */
            vl.addComponent(getSpeciesLocationTable());

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

    private EntityTable<SpeciesImpact> getSpeciesImpactTable() {

        EntityTable<SpeciesImpact> table = new EntityTable<SpeciesImpact>(
                speciesImpactContainer);
        table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);

        table.setHeight("300px");
        table.setWidth("500px");
        table.setPageLength(20);

        table.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {

                Long si = (Long) event.getProperty().getValue();

                // speciesImpactSelected(si);
            }
        });

        ImpactVisualizationColumn generatedColumn = new ImpactVisualizationColumn();
        table.addGeneratedColumn("id", generatedColumn);
        table.setColumnWidth("id", 400);
        return table;

    }

    @Override
    public void enter(ViewChangeEvent event) {

        String params = event.getParameters();

        setEntityId(params);
    }

    private void setEntityId(String entityId) {

        Long id = Long.valueOf(entityId);

        Location loc = dao.find(Location.class, id);

        if (loc == null) {
            Notification.show("Could not find location.");
            return;
        }

        Collection<LeafletLayer> poly = JTSUtil.toLayers(loc.getGeom());

        LFeatureGroup lfg = new LFeatureGroup();
        lfg.addComponent(poly);
        

        map.getMap().addComponent(lfg);
        map.zoomTo(loc.getEnvelope());

        /*
         * TODO: so much duplication
         * 
         * Mess with location types
         */

        String desc = "";
        if (loc.getPrefix().equals("WDPA")) {
            desc = " " + loc.getDesignation();
            locationDescription.setValue("IUCN category " + loc.getIucnCat());
        }

        locationName.setValue(loc.getName() + desc);

        // locationDescription.setValue(desc);

        speciesImpactContainer.removeAllContainerFilters();
        speciesImpactContainer.addContainerFilter(new Compare.Equal(
                SpeciesImpact_.location.getName(), loc));

        speciesLocationContainer.removeAllContainerFilters();
        speciesLocationContainer.addContainerFilter(new Compare.Equal(
                SpeciesLocation_.location.getName(), loc));

        ls.setLocation(loc);

        Polygon polyEnv = loc.getEnvelope();
        map.zoomTo(polyEnv);
    }

}
