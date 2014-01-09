package org.issg.ibis.client;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.LWmsLayer;
import org.vaadin.addon.leaflet.shared.Bounds;

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

}
