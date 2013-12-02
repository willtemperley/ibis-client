package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(IslandType.class)
public abstract class IslandType_ {

    public static volatile SingularAttribute<IslandType,String> description;

    public static volatile SingularAttribute<IslandType,Long> id;
}
