package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Genus.class)
public abstract class Genus_ {

    public static volatile SingularAttribute<Genus,String> label;

    public static volatile SingularAttribute<Genus,Long> id;

    public static volatile SingularAttribute<Genus,Family> family;
}
