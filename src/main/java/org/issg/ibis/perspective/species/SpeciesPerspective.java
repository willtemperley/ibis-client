package org.issg.ibis.perspective.species;

import it.jrc.auth.RoleManager;
import it.jrc.form.editor.EntityTable;

import java.util.HashSet;
import java.util.Set;

import org.issg.excel.download.ExportBar;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.SpeciesImpactAdapter;
import org.issg.ibis.domain.SpeciesLocation;
import org.issg.ibis.domain.SpeciesLocationAdapter;
import org.issg.ibis.domain.SpeciesLocation_;
import org.issg.ibis.domain.view.SpeciesExtent;
import org.issg.ibis.editor.InlineSpeciesEditor;
import org.issg.ibis.perspective.shared.LayerViewer;
import org.issg.ibis.webservices.ArkiveV1Search;
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

	private ListContainer<SpeciesImpactAdapter> speciesImpactContainer = new ListContainer<SpeciesImpactAdapter>( SpeciesImpactAdapter.class);

	private ListContainer<SpeciesLocationAdapter> speciesLocationContainer = new ListContainer<SpeciesLocationAdapter>(
			SpeciesLocationAdapter.class);

	private SpeciesSummaryController speciesSummary;

	private InlineSpeciesEditor speciesEditor;

	private LayerViewer map;

	private FeatureMapLayer fml = new FeatureMapLayer();

	private LMarkerClusterGroup mapClusterGroup = new LMarkerClusterGroup();
	private Window w;

	private ExportBar exporter = new ExportBar();
	private EntityTable<SpeciesImpactAdapter> speciesImpactTable;
	private TabSheet ts;

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
			this.speciesSummary = new SpeciesSummaryController(leftPanel,
					new ArkiveV1Search(dao));

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
			// legend = new MapLegend();
			// mapLayout.addComponent(legend);

			vl.addComponent(mapLayout);

			/*
			 * Tables in tabsheet
			 */
			ts = new TabSheet();
			ts.setSizeFull();
			speciesImpactTable = getSpeciesImpactTable();
			speciesImpactTable.setItalicColumn("name");
			ts.addTab(speciesImpactTable);
			ts.addTab(getSpeciesLocationAdapterTable(), "Occurrences");
			ts.addTab(exporter, "Download");
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

		table.addColumns("location", "country", "biologicalStatus");
		return table;
	}

	private EntityTable<SpeciesImpactAdapter> getSpeciesImpactTable() {

		EntityTable<SpeciesImpactAdapter> table = new EntityTable<SpeciesImpactAdapter>(
				speciesImpactContainer);

		table.addColumns("name", "commonName", "country", "location");

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

		Species sp = dao.find(Species.class, id);

		this.species = sp;

		if (sp == null) {
			Notification.show("Could not find species: " + id);
			return;
		}
		
		if (species.getIsInvasive()) {
			ts.getTab(speciesImpactTable).setCaption("Native species impacts");
		} else {
			ts.getTab(speciesImpactTable).setCaption("Invasive species impacts");
		}

		exporter.setEntity(sp);

		/*
		 * Species display
		 */
		speciesSummary.setSpecies(sp);

		// Clear other info
		speciesImpactContainer.removeAllItems();

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

		// legend.descr.setValue(lCount + " georeferenced locations shown.");

		if (lCount > 0) {
			map.getMap().zoomToContent(mapClusterGroup);
		}

	}

	private void speciesImpactSelected(SpeciesImpactAdapter si) {

		SpeciesImpact speciesImpact = dao.find(SpeciesImpact.class, si.getId());

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
