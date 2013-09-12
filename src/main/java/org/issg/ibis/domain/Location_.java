package org.issg.ibis.domain;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.jrc.persist.adminunits.Country;

import com.vividsolutions.jts.geom.Polygon;

@StaticMetamodel(Location.class)
public abstract class Location_ {

    public static volatile SingularAttribute<Location,String> locationId;

    public static volatile SingularAttribute<Location,Polygon> geom;

    public static volatile SingularAttribute<Location,String> name;

    public static volatile SingularAttribute<Location,String> localName;

    public static volatile SingularAttribute<Location,Country> country;

    public static volatile SingularAttribute<Location,Long> id;

    public static volatile SingularAttribute<Location,Polygon> envelope;
}
