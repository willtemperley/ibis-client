package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(LocationSummary.class)
public abstract class LocationSummary_ {

    public static volatile SingularAttribute<LocationSummary, ContentType> contentType;

    public static volatile SingularAttribute<LocationSummary, String> content;

    public static volatile SingularAttribute<LocationSummary, Location> location;

    public static volatile SingularAttribute<LocationSummary, Long> id;
}
