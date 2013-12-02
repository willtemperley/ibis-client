package org.issg.ibis.client;

import java.util.List;

import javax.persistence.TypedQuery;

import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Location_;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.SpeciesImpact_;
import org.jrc.form.editor.EntityTable;
import org.jrc.persist.ContainerManager;
import org.jrc.persist.Dao;
import org.jrc.persist.adminunits.Country;
import org.jrc.persist.adminunits.Country_;
import org.jrc.ui.SimpleHtmlHeader;
import org.jrc.ui.SimpleHtmlLabel;
import org.jrc.ui.SimplePanel;
import org.jrc.ui.baseview.TwinPanelView;
import org.vaadin.addon.leaflet.LMarker;

import com.google.inject.Inject;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vividsolutions.jts.geom.Polygon;

public class CountryPerspective extends TwinPanelView implements View {

    private Dao dao;
    private Country country;
    private LayerViewer map;
    private BeanItemContainer<Location> bic = new BeanItemContainer<Location>(Location.class);
    private SimpleHtmlHeader countryName;
    private JPAContainer<SpeciesImpact> speciesImpactContainer;
    private SimpleHtmlLabel locationDescription = new SimpleHtmlLabel();

    /**
     * Displays a threatened species and it's locations.
     * 
     * TODO
     * [ ] Zoom to country control
     * [ ] Species 
     * 
     * @param dao
     * @param speciesInfo
     * @param speciesSelector
     */
    @Inject
    public CountryPerspective(Dao dao) {

        this.dao = dao;
        setExpandRatios(1f, 1.5f);

        {
            /*
             * Country info
             */
            SimplePanel leftPanel = getLeftPanel();
            countryName = new SimpleHtmlHeader();
            countryName.addStyleName("header-large");
            
            {
                leftPanel.addComponent(countryName);
                
//                HorizontalLayout hl = new HorizontalLayout();
//                hl.setWidth("100%");
//                hl.addComponent(getLocationTable());
//                hl.addComponent(locationDescription);
//                
//                leftPanel.addComponent(hl);
            }
            
            {
                SimpleHtmlHeader impacts = new SimpleHtmlHeader("Known Impacts");
                leftPanel.addComponent(impacts);
    
                ContainerManager<SpeciesImpact> containerManager = new ContainerManager<SpeciesImpact>(dao, SpeciesImpact.class);
                this.speciesImpactContainer = containerManager.getContainer();
                leftPanel.addComponent(getSpeciesImpactTable());
            }
            
        }

        {
            SimplePanel rightPanel = getRightPanel();

            VerticalLayout vl = new VerticalLayout();
            vl.setSizeFull();
            
            /*
             * Map
             */
            map = new LayerViewer();
            map.setSizeFull();

            vl.addComponent(map);

            /*
             * Table
             */
            rightPanel.addComponent(vl);
        }
    }

    protected void refresh() {
        
    }

    protected void showWindow() {

    }

    private EntityTable<Location> getLocationTable() {

        EntityTable<Location> table = new EntityTable<Location>(bic);

        table.setHeight("300px");
        table.setPageLength(20);

        table.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {

                Location si = (Location) event.getProperty()
                        .getValue();

                locationSelected(si);
            }
        });

        table.addColumns(Location_.name);
        return table;
    }

    private EntityTable<SpeciesImpact> getSpeciesImpactTable() {
        
        EntityTable<SpeciesImpact> table = new EntityTable<SpeciesImpact>(speciesImpactContainer);
        table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);

        table.setHeight("600px");
        table.setWidth("500px");
        table.setPageLength(20);

        table.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {

                Long si = (Long) event.getProperty().getValue();

//                speciesImpactSelected(si);
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

    private void setEntityId(String id) {

        Country c = dao.findByProxyId(Country_.isoa3Id, id);
        

        this.country = c;

        if (c == null) {
            Notification.show("Could not find country: " + id);
            return;
        }

        /*
         * Country info
         */
        countryName.setValue(c.getName());

        /*
         * Locations
         */
        TypedQuery<Location> q = dao
                .getEntityManager()
                .createQuery(
                        "from Location where country.id = :countryId",
                        Location.class);
        q.setParameter("countryId", country.getId());

        List<Location> res = q.getResultList();
        bic.addAll(res);

        
        for (Location location : res) {
            LMarker marker = JTS2Leaflet.getLMarkerForCentroid(location.getGeom());
            if (marker != null) {
                map.getMap().addComponent(marker);
            }
        }
        
        map.zoomTo(country.getEnvelope());

    }

    private void locationSelected(Location si) {

        if (si == null) {
            return;
        }
        
        locationDescription.setValue(si.getComments());
        
        speciesImpactContainer.removeAllContainerFilters();
//        speciesImpactContainer.addContainerFilter(new Compare.Equal(SpeciesImpact_.location.getName(), si.getId()));

        Polygon polyEnv = si.getEnvelope();
        map.zoomTo(polyEnv);

    }

    class ImpactVisualizationColumn implements Table.ColumnGenerator {

        public Component generateCell(Table source, Object itemId,
                Object columnId) {
            JPAContainerItem<?> item = (JPAContainerItem<?>) source.getItem(itemId);
            final SpeciesImpact si = (SpeciesImpact) item.getEntity();
            return new ImpactVisualization(si);
        }
    }

}
