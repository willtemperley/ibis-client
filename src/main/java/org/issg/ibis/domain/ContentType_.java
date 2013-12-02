package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ContentType.class)
public abstract class ContentType_ {

    public static volatile SingularAttribute<ContentType, String> name;

    public static volatile SingularAttribute<ContentType, Long> id;
}
