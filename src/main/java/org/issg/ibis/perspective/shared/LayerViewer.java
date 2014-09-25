package org.issg.ibis.perspective.shared;

import java.util.Collection;
import java.util.List;
import org.issg.ibis.domain.Location;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.LWmsLayer;
import org.vaadin.addon.leaflet.markercluster.LMarkerClusterGroup;
import org.vaadin.addon.leaflet.shared.Bounds;

import com.vaadin.ui.Panel;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class LayerViewer extends Panel {

	private LMap map;
	private LMarkerClusterGroup baseLocations = new LMarkerClusterGroup();
	private LMarkerClusterGroup searchLocations = new LMarkerClusterGroup();

	public LayerViewer() {

		LTileLayer bl = TileLayerFactory.getOSM();

//		LTileLayer bl = new LTileLayer(
//				"http://otile{s}.mqcdn.com/tiles/1.0.0/sat/{z}/{x}/{y}.jpg");
//		bl.setSubDomains("1", "2", "3", "4");
//		bl.setAttributionString("&copy; <a href='http://osm.org/copyright'>OpenStreetMap</a> contributors");
		
		
		map = new LMap();
		setContent(map);
		map.addBaseLayer(bl, "OSM");

		map.setMaxZoom(15);
	}

	

//	public void addWmsLayer(String layerName) {
//
//		LWmsLayer l = new LWmsLayer();
//		l.setUrl("http://lrm-maps.jrc.ec.europa.eu/geoserver/ibis/wms");
//		l.setTransparent(true);
//		l.setFormat("image/png");
//		l.setLayers(layerName);
//
//		map.addLayer(l);
//	}

	public void zoomTo(Polygon p) {

		if (p == null) {
			return;
		}

		Envelope env = p.getEnvelopeInternal();

		Bounds b = new Bounds();

		double minX = env.getMinX();
		double maxX = env.getMaxX();

		double minY = env.getMinY();
		double maxY = env.getMaxY();

		b.setSouthWestLat(minY);
		b.setNorthEastLat(maxY);

		/*
		 * Super hacky transformation
		 */
		if (minX <= 0) {
			minX += 360;
		}
		if (maxX <= 0) {
			maxX += 360;
		}
		b.setSouthWestLon(minX);
		b.setNorthEastLon(maxX);

		map.zoomToExtent(b);
	}

	public LMap getMap() {
		return map;
	}

	public void setSearchResults(List<Location> results) {

		int nMarkers = 0;

		searchLocations.removeAllComponents();

		for (Location location : results) {

			Point p = location.getCentroid();
			if (p != null) {
				nMarkers++;
				LMarker lMarker = new LMarker(p);
				lMarker.setPopup(location.getName());
				searchLocations.addComponent(lMarker);
			}
		}

		// for (ResourceDescription rd : results) {
		// String x = rd.getId();
		// String[] arr = x.split("/");
		// if (arr[0].equals("Location")) {
		//
		// }
		// Set<Location> locs = rd.getLocations();
		// for (Location loc : locs) {
		// Point p = loc.getCentroid();
		// if (p != null) {
		// nMarkers ++;
		// searchLocations.addComponent(new LMarker(p));
		// }
		// }
		// }

		baseLocations.setVisible(false);
		map.addComponent(searchLocations);
		if (nMarkers > 0) {
			map.zoomToContent(searchLocations);
		}
	}

	public LMarkerClusterGroup addLocations(Collection<Location> loc) {
		this.baseLocations = new LMarkerClusterGroup();

		for (Location location : loc) {
			Point x = location.getCentroid();
			if (x != null) {
				baseLocations.addComponent(new LMarker(x));
			}
		}

		map.addComponent(baseLocations);
		map.zoomToContent(baseLocations);
		return baseLocations;
	}

}
