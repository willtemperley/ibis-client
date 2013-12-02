package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Taxon.class)
public abstract class Taxon_ {

    public static volatile SingularAttribute<Taxon, String> label;

    public static volatile SingularAttribute<Taxon, TaxonomicRank> taxonomicRank;

    public static volatile SingularAttribute<Taxon, Long> id;

    public static volatile SingularAttribute<Taxon, Taxon> parentTaxon;
}
