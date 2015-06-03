package org.biopama.ibis.perspective.location;

import java.util.List;

import org.biopama.edit.Dao;
import org.biopama.ibis.perspective.shared.TwinPanelPerspective;
import org.biopama.ui.HtmlHeader;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.QSpeciesImpact;
import org.issg.ibis.domain.QSpeciesLocation;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.SpeciesLocation;
import org.issg.ibis.domain.adapter.IASAdapter;
import org.issg.ibis.domain.adapter.SpeciesLocationAdapter;
import org.vaadin.addons.lec.EntityTable;
import org.vaadin.maddon.ListContainer;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mysema.query.SearchResults;
import com.mysema.query.jpa.impl.JPAQuery;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vividsolutions.jts.geom.Polygon;

public class LocationPerspective extends TwinPanelPerspective implements View {

	private Dao dao;

	private HtmlHeader locationName;

	private ListContainer<IASAdapter> speciesImpactContainer = new ListContainer<IASAdapter>( IASAdapter.class);
	private ListContainer<SpeciesLocationAdapter> invasiveSpeciesLocationContainer = new ListContainer<SpeciesLocationAdapter>(
			SpeciesLocationAdapter.class);
	private ListContainer<SpeciesLocationAdapter> nativeSpeciesLocationContainer = new ListContainer<SpeciesLocationAdapter>(
			SpeciesLocationAdapter.class);

	private LocationSummaryView ls;
	private LocationDescription locationDescription;

	@Inject
	public LocationPerspective(Dao dao, @Named("context_path") String contextPath) {

		addStyleName("location");
		
		exporter.setBaseUrl(contextPath + "/download/location");

		this.dao = dao;

		{
			CssLayout leftPanel = new CssLayout();

			locationName = new HtmlHeader();
			locationName.addStyleName("header-large");
			locationDescription = new LocationDescription();
			leftPanel.addComponent(locationName);
			leftPanel.addComponent(locationDescription);

            ls = new LocationSummaryView(leftPanel);
			setLeftPanelContent(leftPanel);
		}

		{
			/*
			 * Tables
			 */
			addTable(getNativeSpeciesLocationAdapterTable(), "Native Species");
			addTable(getInvasiveSpeciesLocationAdapterTable(), "Invasive Species");
			addTable(getSpeciesImpactTable(), "Invasive Alien Species Impacts");
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

		table.addColumns("nativeSpecies", "nativeCommonName", "invasiveAlienSpecies", "invasiveAlienCommonName", "impactMechanism", "reference");
		
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

		Page.getCurrent().setTitle(loc.getName());

		getMap().zoomTo(loc.getEnvelope());

		locationName.setValue(loc.getName());

		locationDescription.setLocation(loc);


		/*
		 * Impacts
		 */
		QSpeciesImpact spImpact = QSpeciesImpact.speciesImpact;
		JPAQuery query = new JPAQuery(dao.get());
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
		query = new JPAQuery(dao.get());
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
		getMap().zoomTo(polyEnv);
	}

	private EntityTable<SpeciesLocationAdapter> getNativeSpeciesLocationAdapterTable() {
	
		EntityTable<SpeciesLocationAdapter> table = new EntityTable<SpeciesLocationAdapter>(nativeSpeciesLocationContainer);
	
		table.setSizeFull();
		table.setPageLength(20);
	
		table.addColumns("species", "commonName", "organismType", "redlistCategory", "biologicalStatus");
		table.setItalicColumn("species");
		return table;
	}

	private EntityTable<SpeciesLocationAdapter> getInvasiveSpeciesLocationAdapterTable() {
	
		EntityTable<SpeciesLocationAdapter> table = new EntityTable<SpeciesLocationAdapter>(invasiveSpeciesLocationContainer);
	
		table.setSizeFull();
		table.setPageLength(20);
	
		table.addColumns("species", "commonName", "organismType", "biologicalStatus");
		table.setItalicColumn("species");
		return table;
	}

}
