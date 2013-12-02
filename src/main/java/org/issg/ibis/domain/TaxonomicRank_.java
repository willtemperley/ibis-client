package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(TaxonomicRank.class)
public abstract class TaxonomicRank_ {

    public static volatile SingularAttribute<TaxonomicRank,String> label;

    public static volatile SingularAttribute<TaxonomicRank,Long> id;
}
