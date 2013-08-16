package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Order.class)
public abstract class Order_ {

    public static volatile SingularAttribute<Order,Clazz> class_;

    public static volatile SingularAttribute<Order,String> label;

    public static volatile SingularAttribute<Order,Long> id;
}
