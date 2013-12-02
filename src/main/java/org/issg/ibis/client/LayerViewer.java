package org.issg.ibis.client;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.LWmsLayer;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Polygon;

public class LayerViewer extends Panel {

    private LMap map;

    public LayerViewer() {

        LTileLayer bl = new LTileLayer(
                "http://{s}.tile.osm.org/{z}/{x}/{y}.png");
        bl.setAttributionString("&copy; <a href='http://osm.org/copyright'>OpenStreetMap</a> contributors");
        map = new LMap();
        setContent(map);
        map.addBaseLayer(bl, "OSM");

    }

    public void addWmsLayer(String layerName) {

        LWmsLayer l = new LWmsLayer();
        l.setUrl("http://lrm-maps.jrc.ec.europa.eu/geoserver/ibis/wms");
        l.setTransparent(true);
        l.setFormat("image/png");
        l.setLayers(layerName);
        


        map.addLayer(l);
    }

    public void zoomTo(Polygon p) {

        if (p == null) {
            return;
        }

        Envelope env = p.getEnvelopeInternal();

        Bounds b = new Bounds();

        b.setSouthWestLat(env.getMinY());
        b.setSouthWestLon(env.getMinX());

        b.setNorthEastLat(env.getMaxY());
        b.setNorthEastLon(env.getMaxX());

        if ((env.getMaxX() - env.getMinX()) > 180) {
            System.out.println("dateline bugfix");
            /*
             * Probably crosses the dateline
             */
            // double y = env.centre().y;
            double x = 180 - env.centre().x;

            // Point c = new Point(y, x);
            // map.setCenter(c);

            b.setSouthWestLat(env.getMinY());
            b.setSouthWestLon(x);

            b.setNorthEastLat(env.getMaxY());
            b.setNorthEastLon(x);
        }
        
        System.out.println(b);

        map.zoomToExtent(b);
    }

    public LMap getMap() {
        return map;
    }

}
