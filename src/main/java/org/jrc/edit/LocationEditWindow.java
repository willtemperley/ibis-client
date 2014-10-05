package org.jrc.edit;

import java.util.ArrayList;
import java.util.List;

import org.issg.ibis.perspective.shared.TileLayerFactory;
import org.vaadin.addon.leaflet.LFeatureGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LeafletLayer;
import org.vaadin.addon.leaflet.draw.LDraw;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDeletedEvent;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDeletedListener;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnEvent;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnListener;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureModifiedEvent;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureModifiedListener;
import org.vaadin.addon.leaflet.util.JTSUtil;
import org.vaadin.maddon.fields.MValueChangeListener;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class LocationEditWindow extends Window {

	private final GeometryFactory FACTORY = new GeometryFactory();

	private LMap lmap;
	private MultiPolygon geom;
	private LFeatureGroup lfg = new LFeatureGroup();



	public void setGeom(MultiPolygon geom) {
		if (geom == null) {
			return;
		}
		System.out.print("nGeometries: ");
		System.out.println(geom.getNumGeometries());
		this.geom = geom;
	}
	

	public LocationEditWindow() {

		setCaption("Edit polygons");


		// map
		lmap = getMap();
		lmap.addLayer(lfg);

		setWidth(800, Unit.PIXELS);
		setHeight(600, Unit.PIXELS);

		// setContent(c);


		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		vl.setSpacing(true);

		vl.addComponent(lmap);
		
		setContent(vl);
		vl.setSizeFull();

	}

	private LMap getMap() {
		LMap lmap = new LMap();
		lmap.addBaseLayer(TileLayerFactory.getOSM(), "OSM");
//		lmap.setWidth("400px");
//		lmap.setHeight("400px");
		lmap.setSizeFull();
		LDraw control = new LDraw();
		lmap.addControl(control);
		control.setEditableFeatureGroup(lfg);
		
		control.addFeatureModifiedListener(new FeatureModifiedListener() {
			@Override
			public void featureModified(FeatureModifiedEvent event) {
				buildMP();
			}
		});

		control.addFeatureDrawnListener(new FeatureDrawnListener() {
			
			@Override
			public void featureDrawn(FeatureDrawnEvent event) {
				LeafletLayer drawnFeature = event.getDrawnFeature();

				lfg.addComponent(drawnFeature);
				buildMP();
			}
		});
		
		control.addFeatureDeletedListener(new FeatureDeletedListener() {
			
			@Override
			public void featureDeleted(FeatureDeletedEvent event) {
				lfg.removeComponent(event.getDeletedFeature());
				buildMP();
			}
		});
		return lmap;
	}

	
	private void buildMP() {
		
		List<Polygon> polys = new ArrayList<Polygon>();

		for (Component c: lfg) {
			if (c instanceof LPolygon) {
				if (((LPolygon) c).getPoints().length == 0) {
					//Sometimes there's a spurious point in there!
					continue;
				}
				Polygon poly = JTSUtil.toPolygon((LPolygon) c);
				polys.add(poly);

			}
		}

		System.out.println("npolys: " + polys.size());

		MultiPolygon mp = new MultiPolygon(polys.toArray(new Polygon[polys.size()]), FACTORY);
		setGeom(mp);

	}


	@Override
	public void attach() {
		center();
		lfg.removeAllComponents();
		lfg.addComponent(new LPolygon());
		if (geom != null) {
			lmap.zoomToExtent(geom);
			for (int i = 0; i < geom.getNumGeometries(); i++) {
				Polygon p = (Polygon) geom.getGeometryN(i);
				LPolygon x = JTSUtil.toPolygon(p);
				lfg.addComponent(x);

			}
		}
		super.attach();
	}

	public MultiPolygon getGeom() {
		return geom;
	}

}
