package org.issg.ibis.domain;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ReferenceLink.class)
public abstract class ReferenceLink_ {

    public static volatile SingularAttribute<ReferenceLink,Reference> reference;

    public static volatile SingularAttribute<ReferenceLink,Species> species;

    public static volatile SingularAttribute<ReferenceLink,Long> id;
}
