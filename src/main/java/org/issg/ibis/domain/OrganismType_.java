package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(OrganismType.class)
public abstract class OrganismType_ {

    public static volatile SingularAttribute<OrganismType,Long> id;

    public static volatile SingularAttribute<OrganismType,String> label;
}
