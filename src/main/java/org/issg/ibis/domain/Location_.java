package org.issg.ibis.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.vividsolutions.jts.geom.Polygon;


@StaticMetamodel(Location.class)
public abstract class Location_ {

    public static volatile SingularAttribute<Location, Long> id;

//    public static volatile SingularAttribute<Location, Polygon> geom;

    public static volatile SingularAttribute<Location, String> name;

    public static volatile SingularAttribute<Location, Country> country;

    public static volatile SingularAttribute<Location, Polygon> envelope;

    public static volatile SingularAttribute<Location, String> prefix;

    public static volatile SingularAttribute<Location, String> identifier;

    public static volatile SingularAttribute<Location, String> url;

    public static volatile SingularAttribute<Location, Double> area;

    public static volatile SingularAttribute<Location, Double> latitude;

    public static volatile SingularAttribute<Location, Double> longitude;

    public static volatile SingularAttribute<Location, String> comments;

    public static volatile SingularAttribute<Location, LocationType> locationType;

    public static volatile SingularAttribute<Location, String> islandGroup;

    public static volatile SingularAttribute<Location, IslandType> islandType;

    public static volatile SingularAttribute<Location, DesignatedAreaType> designatedAreaType;

    public static volatile ListAttribute<Location, LocationSummary> locationSummaries;
}
