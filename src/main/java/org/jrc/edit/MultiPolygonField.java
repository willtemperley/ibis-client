package org.jrc.edit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.issg.ibis.perspective.shared.TileLayerFactory;
import org.vaadin.addon.leaflet.LFeatureGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.LeafletLayer;
import org.vaadin.addon.leaflet.util.AbstractJTSField;
import org.vaadin.addon.leaflet.util.JTSUtil;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class MultiPolygonField extends CustomField<MultiPolygon> {

	// private List<LPolygon> lPolygons = new ArrayList<LPolygon>();
	LFeatureGroup lfg = new LFeatureGroup();
	private Button b = new Button("Edit");

	private LocationEditWindow lew;

	public MultiPolygonField() {

		b.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				// issue with state fixed by this
				lew = new LocationEditWindow();
				lew.setGeom(getInternalValue());
				UI.getCurrent().addWindow(lew);

				lew.addCloseListener(new CloseListener() {

					@Override
					public void windowClose(CloseEvent e) {
						// TODO Auto-generated method stub

						MultiPolygon mp = lew.getGeom();
						setInternalValue(mp);
					}
				});

			}
		});

	}


	@Override
	protected Component initContent() {
		VerticalLayout vl = new VerticalLayout();
		vl.addComponent(b);
		// vl.addComponent(map);
		return vl;
	}


	@Override
	public Class<MultiPolygon> getType() {
		return MultiPolygon.class;
	}

}
