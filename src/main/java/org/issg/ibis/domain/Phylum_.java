package org.issg.ibis.domain;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Phylum.class)
public abstract class Phylum_ {

    public static volatile SingularAttribute<Phylum,Kingdom> kingdom;

    public static volatile SingularAttribute<Phylum,String> label;

    public static volatile SingularAttribute<Phylum,Long> id;
}
