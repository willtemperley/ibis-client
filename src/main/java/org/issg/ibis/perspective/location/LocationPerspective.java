package org.issg.ibis.perspective.location;

import it.jrc.form.editor.EntityTable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.QLocation;
import org.issg.ibis.domain.QSpeciesImpact;
import org.issg.ibis.domain.QSpeciesLocation;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.SpeciesImpact_;
import org.issg.ibis.domain.SpeciesLocation;
import org.issg.ibis.domain.SpeciesLocation_;
import org.issg.ibis.perspective.shared.LayerViewer;
import org.jrc.persist.Dao;
import org.jrc.persist.adminunits.Country;
import org.jrc.ui.HtmlHeader;
import org.jrc.ui.HtmlLabel;
import org.jrc.ui.SimplePanel;
import org.jrc.ui.baseview.TwinPanelView;
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
	private Country country;
	private LayerViewer map;
	private BeanItemContainer<SpeciesLocation> bic = new BeanItemContainer<SpeciesLocation>(
			SpeciesLocation.class);
	private HtmlHeader locationName;

	private ListContainer<SpeciesImpact> speciesImpactContainer = new ListContainer<SpeciesImpact>(
			SpeciesImpact.class);
	private ListContainer<SpeciesLocation> speciesLocationContainer = new ListContainer<SpeciesLocation>(
			SpeciesLocation.class);

	private HtmlLabel locationDescription = new HtmlLabel();
	private LocationSummaryView ls;

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
			leftPanel.addComponent(locationName);
			leftPanel.addComponent(locationDescription);

            ls = new LocationSummaryView(leftPanel);

		}

		{
//			SimplePanel rightPanel = getRightPanel();

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
			ts.addTab( getSpeciesImpactTable(), "Impacts");
			ts.addTab(getSpeciesLocationTable(), "Locations");
			vl.addComponent(ts);
			vl.setSpacing(true);

			setExpandRatio(vl, 1);
		}

	}

	private EntityTable<SpeciesLocation> getSpeciesLocationTable() {

		EntityTable<SpeciesLocation> table = new EntityTable<SpeciesLocation>(
				speciesLocationContainer);

		table.setSizeFull();
		table.setPageLength(40);

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

		table.setSizeFull();
		table.setPageLength(40);

		table.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(Property.ValueChangeEvent event) {

				SpeciesImpact si = (SpeciesImpact) event.getProperty()
						.getValue();

				System.out.println(si);
			}
		});

		// ImpactVisualizationColumn generatedColumn = new
		// ImpactVisualizationColumn();
		// table.addGeneratedColumn("id", generatedColumn);
		// table.setColumnWidth("id", 400);
		table.addColumns(
				SpeciesImpact_.threatenedSpecies,
				SpeciesImpact_.invasiveSpecies,
				SpeciesImpact_.impactMechanism, 
				SpeciesImpact_.impactOutcome);
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

		map.zoomTo(loc.getEnvelope());

		/*
		 * TODO: so much duplication
		 * 
		 * Mess with location types
		 */

		String desc = "";
		String prefix = loc.getPrefix();
		if (prefix != null && prefix.equals("WDPA")) {
			desc = " " + loc.getDesignation();
			locationDescription.setValue("IUCN category " + loc.getIucnCat());
		}

		locationName.setValue(loc.getName() + desc);



		/*
		 * Impacts
		 */
		QSpeciesImpact spImpact = QSpeciesImpact.speciesImpact;
		JPAQuery query = new JPAQuery(dao.getEntityManager());
		SearchResults<SpeciesImpact> impacts = query.from(spImpact)
				.where(spImpact.location.eq(loc))
				.listResults(spImpact.speciesImpact);
		speciesImpactContainer.removeAllItems();
		List<SpeciesImpact> resImpacts = impacts.getResults();
		System.out.println("Size: " + resImpacts.size());
		speciesImpactContainer.addAll(resImpacts);

		/*
		 * SpeciesLocations
		 */
		QSpeciesLocation spLoc = QSpeciesLocation.speciesLocation;
		query = new JPAQuery(dao.getEntityManager());
		SearchResults<SpeciesLocation> locations = query.from(spLoc)
				.where(spLoc.location.eq(loc))
				.listResults(spLoc.speciesLocation);
		speciesLocationContainer.removeAllItems();
		List<SpeciesLocation> results = locations.getResults();
		System.out.println("Size: " + results.size());
		speciesLocationContainer.addAll(results);

		/*
		 * Locs
		 */
//		LMarkerClusterGroup lmg = new LMarkerClusterGroup();
//
//		Set<Location> locationSet = new HashSet<Location>();
//		for (SpeciesImpact si : resImpacts) {
//			locationSet.add(si.getLocation());
//		}
//		for (SpeciesLocation sl : results) {
//			locationSet.add(sl.getLocation());
//		}
//		for (Location location : locationSet) {
//			Point c = location.getCentroid();
//			if (c != null) {
//				lmg.addComponent(new LMarker(c));
//			}
//		}
//		map.getMap().addLayer(lmg);

		ls.setLocation(loc);

		Polygon polyEnv = loc.getEnvelope();
		map.zoomTo(polyEnv);
	}

}
