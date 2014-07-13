package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(BiologicalStatus.class)
public abstract class BiologicalStatus_ {

    public static volatile SingularAttribute<BiologicalStatus, String> label;

    public static volatile SingularAttribute<BiologicalStatus, Long> id;
}
