package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Family.class)
public abstract class Family_ {

    public static volatile SingularAttribute<Family,Order> order;

    public static volatile SingularAttribute<Family,String> label;

    public static volatile SingularAttribute<Family,Long> id;
}
