package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(DesignatedAreaType.class)
public abstract class DesignatedAreaType_ {

    public static volatile SingularAttribute<DesignatedAreaType,String> label;

    public static volatile SingularAttribute<DesignatedAreaType,Long> id;
}
