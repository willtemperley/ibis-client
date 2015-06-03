package org.biopama.ibis.perspective.shared;

import org.vaadin.addon.leaflet.LTileLayer;

public class TileLayerFactory {

	public static LTileLayer getOSM() {
		LTileLayer bl = new LTileLayer("http://{s}.tile.osm.org/{z}/{x}/{y}.png");
		bl.setAttributionString("&copy; <a href='http://osm.org/copyright'>OpenStreetMap</a> contributors");
		return bl;
	}

}
