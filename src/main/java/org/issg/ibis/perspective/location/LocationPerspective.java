package org.issg.ibis.perspective.location;

import it.jrc.form.editor.EntityTable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.issg.ibis.domain.Country;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.QLocation;
import org.issg.ibis.domain.QSpeciesImpact;
import org.issg.ibis.domain.QSpeciesLocation;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.SpeciesImpact_;
import org.issg.ibis.domain.SpeciesLocation;
import org.issg.ibis.domain.SpeciesLocation_;
import org.issg.ibis.domain.adapter.IASAdapter;
import org.issg.ibis.domain.adapter.SpeciesImpactAdapter;
import org.issg.ibis.domain.adapter.SpeciesLocationAdapter;
import org.issg.ibis.perspective.shared.LayerViewer;
import org.jrc.edit.Dao;
import org.jrc.ui.HtmlHeader;
import org.jrc.ui.HtmlLabel;
import org.jrc.ui.SimplePanel;
import org.jrc.ui.TwinPanelView;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.markercluster.LMarkerClusterGroup;
import org.vaadin.maddon.ListContainer;

import com.google.inject.Inject;
import com.mysema.query.SearchResults;
import com.mysema.query.jpa.impl.JPAQuery;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class LocationPerspective extends TwinPanelView implements View {

	private Dao dao;
	private LayerViewer map;
	private HtmlHeader locationName;

	private ListContainer<IASAdapter> speciesImpactContainer = new ListContainer<IASAdapter>( IASAdapter.class);

	private ListContainer<SpeciesLocationAdapter> invasiveSpeciesLocationContainer = new ListContainer<SpeciesLocationAdapter>(
			SpeciesLocationAdapter.class);
	private ListContainer<SpeciesLocationAdapter> nativeSpeciesLocationContainer = new ListContainer<SpeciesLocationAdapter>(
			SpeciesLocationAdapter.class);

	private LocationSummaryView ls;
	private LocationDescription locationDescription;

	/**
     */
	@Inject
	public LocationPerspective(Dao dao) {

		this.dao = dao;

		{
			/*
			 * Location info
			 */
			SimplePanel leftPanel = getLeftPanel();
			leftPanel.setWidth("600px");
			locationName = new HtmlHeader();
			locationName.addStyleName("header-large");
			locationDescription = new LocationDescription();
			locationDescription = new LocationDescription();
			leftPanel.addComponent(locationName);
			leftPanel.addComponent(locationDescription);

            ls = new LocationSummaryView(leftPanel);
		}

		{
			VerticalLayout vl = new VerticalLayout();
			vl.setSizeFull();
			
			replaceComponent(getRightPanel(), vl);

			/*
			 * Map
			 */
			map = new LayerViewer();
			map.setSizeFull();

			vl.addComponent(map);

			/*
			 * Table
			 */
			TabSheet ts = new TabSheet();
			ts.setSizeFull();
			ts.addTab(getSpeciesImpactTable(), "Invasive Alien Species Impacts");
			ts.addTab(getNativeSpeciesLocationAdapterTable(), "Native Species");
			ts.addTab(getInvasiveSpeciesLocationAdapterTable(), "Alien and Invasive Species");
			vl.addComponent(ts);
			vl.setSpacing(true);

			setExpandRatio(vl, 1);
		}

	}

	@Override
	public void enter(ViewChangeEvent event) {
	
		String params = event.getParameters();
	
		setEntityId(params);
	}

	private EntityTable<IASAdapter> getSpeciesImpactTable() {

		EntityTable<IASAdapter> table = new EntityTable<IASAdapter>(
				speciesImpactContainer);

		table.setSizeFull();
		table.setPageLength(40);

		table.addColumns("nativeSpecies", "nativeCommonName", "invasiveAlienSpecies", "invasiveAlienCommonName", "impactMechanism", "impactOutcome", "reference");
		
		table.setItalicColumn("nativeSpecies", "invasiveAlienSpecies");

		return table;

	}

	private void setEntityId(String entityId) {

		Long id = Long.valueOf(entityId);

		Location loc = dao.find(Location.class, id);

		if (loc == null) {
			Notification.show("Could not find location.");
			return;
		}

		map.zoomTo(loc.getEnvelope());

		locationName.setValue(loc.getName());

		locationDescription.setLocation(loc);


		/*
		 * Impacts
		 */
		QSpeciesImpact spImpact = QSpeciesImpact.speciesImpact;
		JPAQuery query = new JPAQuery(dao.getEntityManager());
		SearchResults<SpeciesImpact> impacts = query.from(spImpact)
				.where(spImpact.location.eq(loc))
				.listResults(spImpact);
		speciesImpactContainer.removeAllItems();
		List<SpeciesImpact> resImpacts = impacts.getResults();
		for (SpeciesImpact speciesImpact : resImpacts) {
			speciesImpactContainer.addItem(new IASAdapter(speciesImpact));
		}

		/*
		 * SpeciesLocations
		 */
		QSpeciesLocation spLoc = QSpeciesLocation.speciesLocation;
		query = new JPAQuery(dao.getEntityManager());
		SearchResults<SpeciesLocation> locations = query.from(spLoc)
				.where(spLoc.location.eq(loc))
				.listResults(spLoc);

		invasiveSpeciesLocationContainer.removeAllItems();
		List<SpeciesLocation> results = locations.getResults();
		for (SpeciesLocation speciesLocation : results) {
			SpeciesLocationAdapter sla = new SpeciesLocationAdapter(speciesLocation);
			if (speciesLocation.getBiologicalStatus().getIsInvasive()) {
				invasiveSpeciesLocationContainer.addItem(sla);
			} else {
				nativeSpeciesLocationContainer.addItem(sla);
			}
		}

		ls.setLocation(loc);

		Polygon polyEnv = loc.getEnvelope();
		map.zoomTo(polyEnv);
	}

	private EntityTable<SpeciesLocationAdapter> getNativeSpeciesLocationAdapterTable() {
	
		EntityTable<SpeciesLocationAdapter> table = new EntityTable<SpeciesLocationAdapter>(nativeSpeciesLocationContainer);
	
		table.setHeight("100%");
		table.setWidth("100%");
		table.setPageLength(20);
	
		table.addColumns("species", "commonName", "redlistCategory", "biologicalStatus");
		table.setItalicColumn("species");
		return table;
	}

	private EntityTable<SpeciesLocationAdapter> getInvasiveSpeciesLocationAdapterTable() {
	
		EntityTable<SpeciesLocationAdapter> table = new EntityTable<SpeciesLocationAdapter>(invasiveSpeciesLocationContainer);
	
		table.setHeight("100%");
		table.setWidth("100%");
		table.setPageLength(20);
	
		table.addColumns("species", "commonName", "biologicalStatus");
		table.setItalicColumn("species");
		return table;
	}

}
