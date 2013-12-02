package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(IucnCategory.class)
public abstract class IucnCategory_ {

    public static volatile SingularAttribute<IucnCategory, String> id;

    public static volatile SingularAttribute<IucnCategory, String> name;

    public static volatile SingularAttribute<IucnCategory, String> description;
}
