package org.issg.ibis.perspective.shared;

import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.shared.Point;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

public class JTS2Leaflet {

    public static LMarker getLMarkerForCentroid(Geometry geom) {
        if (geom == null)
            return null;
        com.vividsolutions.jts.geom.Point pt = geom.getCentroid();

        //horrid hack for dateline wrapping
        double x = pt.getX();
        if (x < 0)  {
            x = 360 + x;
//            System.out.println(x);
        }

        Point point = new Point(pt.getY(), x);

        LMarker lm = new LMarker(point);
        return lm;
    }

//    public static LPolygon getLPolygon(Geometry geom) {
//    
//        if (geom instanceof MultiPolygon) {
//            MultiPolygon mp = (MultiPolygon) geom;
//            Coordinate[] coords = mp.getBoundary().getCoordinates();
//            Point[] points = new Point[coords.length];
//            for (int i = 0; i < coords.length; i++) {
//                points[i] = new Point(coords[i].y, coords[i].x);
//            }
//            LPolygon lp = new LPolygon(points);
//            return lp;
//        }
//        return null;
//    }

}
