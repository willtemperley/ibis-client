package org.issg.ibis.editor;

import java.util.List;

import org.issg.ibis.domain.Country;
import org.issg.ibis.domain.GID;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.QGID;
import org.issg.ibis.domain.QLocation;
import org.issg.ibis.perspective.shared.LayerViewer;
import org.issg.ibis.perspective.shared.TileLayerFactory;
import org.jrc.edit.Dao;
import org.jrc.edit.EditorController;
import org.vaadin.addon.leaflet.LFeatureGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.LeafletLayer;
import org.vaadin.addon.leaflet.markercluster.LMarkerClusterGroup;
import org.vaadin.maddon.fields.MValueChangeEvent;
import org.vaadin.maddon.fields.MValueChangeListener;
import org.vaadin.maddon.fields.TypedSelect;

import com.mysema.query.jpa.impl.JPAQuery;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class LocationSearchWindow extends Window {

	private LocationInfo locationInfo = new LocationInfo();

	/**
	 * Just to preview the location
	 */
	private class LocationInfo extends VerticalLayout {

		private Label label = new Label();
		private Label lat = new Label();
		private Label lon = new Label();
		private GID loc;

		public LocationInfo() {
			label.setCaption("Island:");
			lat.setCaption("Lat:");
			lon.setCaption("Lon:");
			addComponent(label);
			addComponent(lat);
			addComponent(lon);
		}

		public void setValue(GID loc) {
			this.loc = loc;
			label.setValue(loc.toString());

			lat.setValue(String.valueOf(loc.getGeom().getX()));
			lon.setValue(String.valueOf(loc.getGeom().getY()));
			setVisible(true);
		}

		public GID getValue() {
			return loc;
		}
	}

	public LocationSearchWindow(final EditorController<Location> ec,
			final Dao dao) {

		setCaption("Import location");

		HorizontalLayout hl = new HorizontalLayout();
		setContent(hl);

		// map
		final LMap lmap = getMap();
		final LFeatureGroup lfg = new LMarkerClusterGroup();
		lmap.addLayer(lfg);

		setWidth(600, Unit.PIXELS);
		setHeight(600, Unit.PIXELS);

		TabSheet tabSheet = new TabSheet();
		hl.addComponent(tabSheet);
		hl.setSpacing(true);
		// setContent(c);

		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		vl.setSpacing(true);
		hl.addComponent(vl);
		hl.addComponent(locationInfo);

		tabSheet.addTab(vl, "GID");

		final TypedSelect<GID> gidSelect = new TypedSelect<GID>(GID.class)
				.withCaption("Island").withSelectType(ComboBox.class);
		gidSelect.addMValueChangeListener(new MValueChangeListener<GID>() {
			@Override
			public void valueChange(MValueChangeEvent<GID> event) {
				locationInfo.setValue(event.getValue());
				lmap.setCenter(event.getValue().getGeom());
			}
		});

		// countries
		List<Country> countries = dao.all(Country.class);
		TypedSelect<Country> countryCombo = new TypedSelect<Country>(
				"Select country").withSelectType(ComboBox.class);
		countryCombo.setOptions(countries);
		vl.addComponent(countryCombo);
		countryCombo
				.addMValueChangeListener(new MValueChangeListener<Country>() {

					@Override
					public void valueChange(MValueChangeEvent<Country> event) {

						Country c = event.getValue();
						if (c != null) {
							JPAQuery q = new JPAQuery(dao.get());
							q = q.from(QGID.gID).where(QGID.gID.country.eq(c));
							List<GID> locs = q.list(QGID.gID);
							lfg.removeAllComponents();
							for (final GID gid : locs) {
								LMarker lmarker = new LMarker(gid.getGeom());
								lmarker.setPopup(gid.getName());
								lmarker.addClickListener(new LeafletClickListener() {
									@Override
									public void onClick(LeafletClickEvent event) {
										locationInfo.setValue(gid);
										gidSelect.setValue(gid);
									}
								});
								lfg.addComponent(lmarker);
							}
							if (locs.size() > 0) {
								lmap.zoomToContent(lfg);
							}
							gidSelect.setOptions(locs);
						}
					}
				});

		vl.addComponent(gidSelect);

		vl.addComponent(lmap);

		Button selectButton = new Button("Select");
		selectButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		locationInfo.addComponent(selectButton);

		selectButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				GID val = locationInfo.getValue();
				if (val != null) {
					Location entity = ec.getEntity();
					entity.populate(val);
					ec.setEntity(entity);
					LocationSearchWindow.this.close();
				}
			}
		});

	}

	private LMap getMap() {
		LMap lmap = new LMap();
		lmap.addBaseLayer(TileLayerFactory.getOSM(), "OSM");
		lmap.setWidth("300px");
		lmap.setHeight("300px");
		return lmap;
	}
	
	@Override
	public void attach() {
		locationInfo.setVisible(false);
		super.attach();
	}

	private Component getDesignatedAreaCombo() {
		return new ComboBox();
	}

}
