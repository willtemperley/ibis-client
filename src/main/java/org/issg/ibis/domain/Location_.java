package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.jrc.persist.adminunits.Country;

@StaticMetamodel(Location.class)
public abstract class Location_ {

    public static volatile SingularAttribute<Location, String> locationId;

    public static volatile SingularAttribute<Location,String> name;

    public static volatile SingularAttribute<Location,String> localName;

    public static volatile SingularAttribute<Location,Country> country;

    public static volatile SingularAttribute<Location,Long> id;
}
