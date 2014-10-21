package org.issg.ibis.perspective.species;

import java.util.HashSet;
import java.util.Set;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.SpeciesLocation;
import org.issg.ibis.domain.adapter.SpeciesImpactAdapter;
import org.issg.ibis.domain.adapter.SpeciesLocationAdapter;
import org.issg.ibis.domain.view.SpeciesExtent;
import org.issg.ibis.perspective.shared.TwinPanelPerspective;
import org.issg.ibis.webservices.ArkiveV1Search;
import org.jrc.edit.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.markercluster.LMarkerClusterGroup;
import org.vaadin.addons.lec.EntityTable;
import org.vaadin.maddon.ListContainer;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet.Tab;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class SpeciesPerspective extends TwinPanelPerspective implements View {

	private Logger logger = LoggerFactory.getLogger(SpeciesPerspective.class);

	private Dao dao;

	private ListContainer<SpeciesImpactAdapter> speciesImpactContainer = new ListContainer<SpeciesImpactAdapter>( SpeciesImpactAdapter.class);

	private ListContainer<SpeciesLocationAdapter> speciesLocationContainer = new ListContainer<SpeciesLocationAdapter>(
			SpeciesLocationAdapter.class);

	private SpeciesSummaryController speciesSummary;

	private LMarkerClusterGroup mapClusterGroup = new LMarkerClusterGroup();

	private Tab impactTab;

	private EntityTable<SpeciesImpactAdapter> speciesImpactTable;
	

	/**
	 * Displays a threatened species and its locations.
	 * 
	 * @param dao
	 * @param speciesSelector
	 */
	@Inject
	public SpeciesPerspective(Dao dao, @Named("context_path") String contextPath, RoleManager roleManager) {

		exporter.setBaseUrl(contextPath + "/download/species");

		this.dao = dao;

		this.speciesSummary = new SpeciesSummaryController(new ArkiveV1Search(dao));
		setLeftPanelContent(speciesSummary);

		//pretty ugly
		getMap().getMap().addComponent(mapClusterGroup);


		speciesImpactTable = getSpeciesImpactTable();
		EntityTable<SpeciesLocationAdapter> speciesLocationTable = getSpeciesLocationAdapterTable();
		speciesImpactTable.setItalicColumn("name");

		addTable(speciesLocationTable, "Occurrences");
		addTable(speciesImpactTable, "");

		impactTab = getTab(speciesImpactTable);
		
	}

	private EntityTable<SpeciesLocationAdapter> getSpeciesLocationAdapterTable() {

		EntityTable<SpeciesLocationAdapter> table = new EntityTable<SpeciesLocationAdapter>(
				speciesLocationContainer);

		table.setHeight("100%");
		table.setWidth("100%");
		table.setPageLength(20);

		table.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(Property.ValueChangeEvent event) {

			}
		});

		table.addColumns("country", "location", "locationType", "biologicalStatus");
		return table;
	}

	private EntityTable<SpeciesImpactAdapter> getSpeciesImpactTable() {

		EntityTable<SpeciesImpactAdapter> table = new EntityTable<SpeciesImpactAdapter>(
				speciesImpactContainer);

		table.addColumns("name", "commonName", "country", "location", "locationType", "impactMechanism", "reference");

		table.setHeight("100%");
		table.setWidth("100%");
		table.setPageLength(20);

		table.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(Property.ValueChangeEvent event) {

				SpeciesImpactAdapter si = (SpeciesImpactAdapter) event.getProperty()
						.getValue();
				speciesImpactSelected(si);

			}
		});

		return table;

	}

	@Override
	public void enter(ViewChangeEvent event) {

		String params = event.getParameters();
		setSpeciesId(params);
	}

	private void setSpeciesId(String params) {
		Long id = Long.valueOf(params);

		Species species = dao.find(Species.class, id);
		SpeciesExtent se = dao.find(SpeciesExtent.class, id);

		if (species == null) {
			Notification.show("Could not find species: " + id);
			return;
		}
		
		Page.getCurrent().setTitle(species.toString());
		
		
		setStyles(species.getIsInvasive());

		if (species.getIsInvasive()) {
			impactTab.setCaption("Native species impacts");
			speciesImpactTable.setColumnHeader("name", "Native species");
		} else {
			impactTab.setCaption("Invasive alien species impacts");
			speciesImpactTable.setColumnHeader("name", "Invasive alien species");
		}

		exporter.setResourceId(species.getId());

		/*
		 * Species display
		 */
		speciesSummary.setSpecies(species);
		
		// Clear other info
		speciesImpactContainer.removeAllItems();
		speciesLocationContainer.removeAllItems();

		/*
		 * Impacts
		 */
		speciesImpactContainer.addAll(species.getSpeciesImpactAdapters());

		Set<SpeciesLocation> speciesLocations = species.getSpeciesLocations();
		for (SpeciesLocation speciesLocation : speciesLocations) {
			speciesLocationContainer.addItem(new SpeciesLocationAdapter(speciesLocation));
		}

		mapClusterGroup.removeAllComponents();

		Set<SpeciesImpact> speciesImpacts = species.getNativeSpeciesImpacts();
		Set<Location> locationSet = new HashSet<Location>();
		for (SpeciesImpact si : speciesImpacts) {
			locationSet.add(si.getLocation());
		}
		for (SpeciesLocation sl : speciesLocations) {
			locationSet.add(sl.getLocation());
		}
		
		
		for (Location location : locationSet) {
			Point point = location.getCentroid();
			if (point != null) {
				LMarker lMarker = new LMarker(point);
				lMarker.setPopup(location.getName());
				mapClusterGroup.addComponent(lMarker);
			}
		}

		// legend.descr.setValue(lCount + " georeferenced locations shown.");

		if (se.getEnvelope() != null) {
			getMap().zoomTo(se.getEnvelope());
		}

	}

	private void setStyles(boolean isInvasive) {
		String nativeStyle = "native-species";
		String invasiveStyle = "invasive-species";
		if (isInvasive) {
			removeStyleName(nativeStyle);
			addStyleName(invasiveStyle);
		} else {
			removeStyleName(invasiveStyle);
			addStyleName(nativeStyle);
		}
	}

	private void speciesImpactSelected(SpeciesImpactAdapter si) {

		if (si == null) {
			return;
		}
		SpeciesImpact speciesImpact = dao.find(SpeciesImpact.class, si.getId());

		Location location = speciesImpact.getLocation();
		if (location == null) {
			logger.debug("Null loc");
			return;
		}

		Polygon env = location.getEnvelope();

		if (env != null) {
			getMap().zoomTo(env);
		}

	}

//	private void zoomToSpeciesExtent() {
//		SpeciesExtent extent = dao.find(SpeciesExtent.class, species.getId());
//		if (extent != null) {
//			getMap().zoomTo(extent.getEnvelope());
//		}
//	}

}
