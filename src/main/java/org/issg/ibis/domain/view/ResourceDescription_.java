package org.issg.ibis.domain.view;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.issg.ibis.domain.Location;

@StaticMetamodel(ResourceDescription.class)
public abstract class ResourceDescription_ {

    public static volatile SingularAttribute<ResourceDescription, String> id;

    public static volatile SingularAttribute<ResourceDescription, String> name;

    public static volatile SingularAttribute<ResourceDescription, Integer> impactCount;

    public static volatile SetAttribute<ResourceDescription, Location> location;

}
