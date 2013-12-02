package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ImpactMechanism.class)
public abstract class ImpactMechanism_ {

    public static volatile SingularAttribute<ImpactMechanism,Long> id;

    public static volatile SingularAttribute<ImpactMechanism,String> label;

    public static volatile SingularAttribute<ImpactMechanism,Boolean> used;
}
