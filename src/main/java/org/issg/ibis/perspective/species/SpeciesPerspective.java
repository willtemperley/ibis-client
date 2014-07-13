package org.issg.ibis.perspective.species;

import it.jrc.form.editor.EntityTable;

import java.util.HashSet;
import java.util.Set;

import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.SpeciesLocation;
import org.issg.ibis.domain.SpeciesLocation_;
import org.issg.ibis.domain.adapter.SpeciesImpactAdapter;
import org.issg.ibis.domain.adapter.SpeciesLocationAdapter;
import org.issg.ibis.domain.view.SpeciesExtent;
import org.issg.ibis.editor.InlineSpeciesEditor;
import org.issg.ibis.perspective.shared.LayerViewer;
import org.issg.ibis.webservices.ArkiveV1Search;
import org.issg.ibis.webservices.ExportBar;
import org.jrc.edit.Dao;
import org.jrc.edit.RoleManager;
import org.jrc.ui.SimplePanel;
import org.jrc.ui.TwinPanelView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.leaflet.LMarker;
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
import com.vaadin.ui.TabSheet.Tab;
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


	private LMarkerClusterGroup mapClusterGroup = new LMarkerClusterGroup();
	
	private Window w;

	private ExportBar exporter = new ExportBar();

	private EntityTable<SpeciesImpactAdapter> speciesImpactTable;

	private TabSheet ts;

//	private Refs refs;

	/**
	 * Displays a threatened species and its locations.
	 * 
	 * @param dao
	 * @param speciesSelector
	 */
	@Inject
	public SpeciesPerspective(Dao dao, RoleManager roleManager) {

		this.dao = dao;

		this.speciesEditor = new InlineSpeciesEditor(Species.class, dao);

		{
			/*
			 * Species info
			 */
			this.speciesSummary = new SpeciesSummaryController(new ArkiveV1Search(dao));

			replaceComponent(getLeftPanel(), speciesSummary);
			speciesSummary.addStyleName("display-panel");
			speciesSummary.setWidth("600px");
			speciesSummary.setHeight("100%");

		}

		{
			VerticalLayout vl = new VerticalLayout();
			vl.setSizeFull();

			/*
			 * Map
			 */
			map = new LayerViewer();
			map.setSizeFull();
			map.getMap().addComponent(mapClusterGroup);

			HorizontalLayout mapLayout = new HorizontalLayout();
			mapLayout.setSpacing(true);
			mapLayout.setSizeFull();
			mapLayout.addComponent(map);

			vl.addComponent(mapLayout);

			/*
			 * Tables in tabsheet
			 */
			ts = new TabSheet();

			HorizontalLayout tableLayout = new HorizontalLayout();
			vl.addComponent(tableLayout);
			tableLayout.addComponent(ts);
			tableLayout.setSizeFull();

			tableLayout.addComponent(exporter);
			exporter.setWidth("40px");
			tableLayout.setExpandRatio(ts, 1);
		
			ts.setSizeFull();
			speciesImpactTable = getSpeciesImpactTable();
			speciesImpactTable.setItalicColumn("name");
			ts.addTab(getSpeciesLocationAdapterTable(), "Occurrences");
			ts.addTab(speciesImpactTable);
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

		table.addColumns("country", "location", "biologicalStatus");
		return table;
	}

	private EntityTable<SpeciesImpactAdapter> getSpeciesImpactTable() {

		EntityTable<SpeciesImpactAdapter> table = new EntityTable<SpeciesImpactAdapter>(
				speciesImpactContainer);

		table.addColumns("name", "commonName", "country", "location", "impactMechanism", "reference");

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
		
		Tab tab = ts.getTab(speciesImpactTable);
		setStyles(species.getIsInvasive());

		if (species.getIsInvasive()) {
			tab.setCaption("Native species impacts");
		} else {
			tab.setCaption("Invasive species impacts");
		}

		exporter.setEntity(sp);

		/*
		 * Species display
		 */
		speciesSummary.setSpecies(sp);
		
//		refs.setReferences(sp.getReferences());

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

	private void setStyles(boolean isInvasive) {
		String nativeStyle = "native-species";
		String invasiveStyle = "invasive-species";
		if (isInvasive) {
			ts.removeStyleName(nativeStyle);
			ts.addStyleName(invasiveStyle);
			speciesSummary.removeStyleName(nativeStyle);
			speciesSummary.addStyleName(invasiveStyle);
		} else {
			ts.removeStyleName(invasiveStyle);
			ts.addStyleName(nativeStyle);
			speciesSummary.addStyleName(nativeStyle);
			speciesSummary.removeStyleName(invasiveStyle);
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
