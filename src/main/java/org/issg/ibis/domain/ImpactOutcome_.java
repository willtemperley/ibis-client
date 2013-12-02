package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ImpactOutcome.class)
public abstract class ImpactOutcome_ {

    public static volatile SingularAttribute<ImpactOutcome,Long> id;

    public static volatile SingularAttribute<ImpactOutcome,String> label;
}
