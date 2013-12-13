package org.issg.ibis.domain;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Reference.class)
public abstract class Reference_ {

    public static volatile SingularAttribute<Reference, String> content;

    public static volatile SingularAttribute<Reference, Long> id;

    public static volatile SetAttribute<Reference, Species> species;

    public static volatile SetAttribute<Reference, Location> locations;
}
