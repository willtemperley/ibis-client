package org.issg.ibis.domain.view;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.issg.ibis.domain.Location;

@StaticMetamodel(LocationView.class)
public abstract class LocationView_ {

    public static volatile SingularAttribute<LocationView, Long> id;

    public static volatile SingularAttribute<LocationView, String> name;

    public static volatile SingularAttribute<LocationView, String> country;

}
