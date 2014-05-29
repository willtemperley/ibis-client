package org.issg.ibis.perspective.species;

import it.jrc.auth.RoleManager;
import it.jrc.form.editor.EntityTable;

import java.util.HashSet;
import java.util.Set;

import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.SpeciesImpact_;
import org.issg.ibis.domain.SpeciesLocation;
import org.issg.ibis.domain.SpeciesLocation_;
import org.issg.ibis.domain.view.SpeciesExtent;
import org.issg.ibis.editor.InlineSpeciesEditor;
import org.issg.ibis.perspective.shared.LayerViewer;
import org.jrc.persist.Dao;
import org.jrc.ui.SimplePanel;
import org.jrc.ui.baseview.TwinPanelView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.jts.FeatureMapLayer;
import org.vaadin.addon.leaflet.markercluster.LMarkerClusterGroup;
import org.vaadin.maddon.ListContainer;

import com.google.inject.Inject;
import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class SpeciesPerspective extends TwinPanelView implements View {

	private Logger logger = LoggerFactory.getLogger(SpeciesPerspective.class);
	private Dao dao;
	private Species species;

	private ListContainer<SpeciesImpact> speciesImpactContainer = new ListContainer<SpeciesImpact>(SpeciesImpact.class);

	private ListContainer<SpeciesLocation> speciesLocationContainer = new ListContainer<SpeciesLocation>(SpeciesLocation.class);

	private SpeciesSummaryController speciesSummary;

	private InlineSpeciesEditor speciesEditor;

	private LayerViewer map;

	private FeatureMapLayer fml = new FeatureMapLayer();

	private MapLegend legend;
	private LMarkerClusterGroup mapClusterGroup = new LMarkerClusterGroup();
	private Window w;

	/**
	 * Displays a threatened species and its locations.
	 * 
	 * @param dao
	 * @param speciesSelector
	 */
	@Inject
	public SpeciesPerspective(Dao dao, RoleManager roleManager) {

		setId("species-perspective");

		this.dao = dao;

		this.speciesEditor = new InlineSpeciesEditor(Species.class, dao);

		{
			/*
			 * Species info
			 */
			SimplePanel leftPanel = getLeftPanel();
			leftPanel.setWidth("600px");
			this.speciesSummary = new SpeciesSummaryController(leftPanel);

			if (roleManager.getRole().getIsSuperUser()) {
				addEditButton(leftPanel);
			}
		}

		{
			VerticalLayout vl = new VerticalLayout();
			vl.setSizeFull();

			/*
			 * Map
			 */
			map = new LayerViewer();
			map.getMap().addComponent(fml);
			map.setSizeFull();
			map.getMap().addComponent(mapClusterGroup);

			HorizontalLayout mapLayout = new HorizontalLayout();
			mapLayout.setSpacing(true);
			mapLayout.setSizeFull();
			mapLayout.addComponent(map);
			legend = new MapLegend();
			mapLayout.addComponent(legend);

			vl.addComponent(mapLayout);

			/*
			 * Tables in tabsheet
			 */
			TabSheet ts = new TabSheet();
			ts.setSizeFull();
			ts.addTab(getSpeciesImpactTable(), "Impacts");
			ts.addTab(getSpeciesLocationTable(), "Locations");
			vl.addComponent(ts);
			vl.setSpacing(true);

			/*
			 * Replace right panel
			 */
			replaceComponent(getRightPanel(), vl);
			setExpandRatio(vl, 1);
		}
	}

	private void addEditButton(SimplePanel leftPanel) {
		Button editButton = leftPanel
				.addActionButton(SimplePanel.ButtonIcon.icon_cog);
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
		if (w == null) {
			w = new Window();
			w.setModal(true);
			w.setWidth("728px");
			w.setHeight("450px");

			w.setContent(speciesEditor);
		}
		UI.getCurrent().addWindow(w);

		species = dao.find(Species.class, species.getId());
		speciesEditor.doUpdate(species);
		// speciesEditor.edit(dao.find(Species.class, species.getId()));
	}

	@SuppressWarnings("unchecked")
	private EntityTable<SpeciesLocation> getSpeciesLocationTable() {

		EntityTable<SpeciesLocation> table = new EntityTable<SpeciesLocation>(
				speciesLocationContainer);

		table.setHeight("300px");
		table.setPageLength(20);

		table.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(Property.ValueChangeEvent event) {

			}
		});

		table.addColumns(SpeciesLocation_.species, SpeciesLocation_.location,
				SpeciesLocation_.biologicalStatus);
		return table;
	}

	@SuppressWarnings("unchecked")
	private EntityTable<SpeciesImpact> getSpeciesImpactTable() {

		EntityTable<SpeciesImpact> table = new EntityTable<SpeciesImpact>(
				speciesImpactContainer);

		table.addColumns(SpeciesImpact_.invasiveSpecies,
				SpeciesImpact_.location, SpeciesImpact_.impactMechanism,
				SpeciesImpact_.impactOutcome);

		table.setHeight("100%");
		table.setWidth("100%");
		table.setPageLength(20);

		table.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(Property.ValueChangeEvent event) {

				SpeciesImpact si = (SpeciesImpact) event.getProperty()
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

		Species sp = dao.find(Species.class, id);

		this.species = sp;

		if (sp == null) {
			Notification.show("Could not find species: " + id);
			return;
		}

		/*
		 * Species display
		 */
		speciesSummary.setSpecies(sp);

		// Clear other info
		speciesImpactContainer.removeAllItems();

		/*
		 * Impacts
		 */
		Set<SpeciesImpact> speciesImpacts = species.getSpeciesImpacts();
		speciesImpactContainer.addAll(speciesImpacts);

		// map.getMap().addLayer(lmg);
		// LMarkerClusterGroup lmg = new LMarkerClusterGroup();
		// for (SpeciesImpact speciesImpact : speciesImpacts) {
		// Location l = speciesImpact.getLocation();
		// Point centroid = l.getCentroid();
		// if (centroid != null) {
		// lmg.addComponent(new LMarker(centroid));
		// }
		// }
		// map.getMap().addComponent(lmg);

		Set<SpeciesLocation> speciesLocations = species.getSpeciesLocations();
		speciesLocationContainer.addAll(speciesLocations);

		mapClusterGroup.removeAllComponents();

		Set<Location> locationSet = new HashSet<Location>();
		for (SpeciesImpact si : speciesImpacts) {
			locationSet.add(si.getLocation());
		}
		for (SpeciesLocation sl : speciesLocations) {
			locationSet.add(sl.getLocation());
		}
		int lCount = 0;
		for (Location location : locationSet) {
			Point point = location.getCentroid();
			if (point != null) {
				lCount++;
				LMarker lMarker = new LMarker(point);
				lMarker.setPopup(location.getName());
				mapClusterGroup.addComponent(lMarker);
			}
		}

		legend.descr.setValue(lCount + " georeferenced locations shown.");

		if (lCount > 0) {
			map.getMap().zoomToContent(mapClusterGroup);
		}

	}

	private void speciesImpactSelected(SpeciesImpact speciesImpact) {

		if (speciesImpact == null) {
			legend.showMapLegend();
			return;
		} else {
			legend.showSpeciesImpact(speciesImpact);
		}

		Location location = speciesImpact.getLocation();
		if (location == null) {
			logger.debug("Null loc");
			return;
		}

		Polygon env = location.getEnvelope();

		if (env != null) {
			map.zoomTo(env);
		}

	}

	private void zoomToSpeciesExtent() {
		SpeciesExtent extent = dao.find(SpeciesExtent.class, species.getId());
		if (extent != null) {
			map.zoomTo(extent.getEnvelope());
		}
	}

}