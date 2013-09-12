package org.issg.ibis.domain;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Kingdom.class)
public abstract class Kingdom_ {

    public static volatile SingularAttribute<Kingdom,Long> id;

    public static volatile SingularAttribute<Kingdom,String> label;
}
