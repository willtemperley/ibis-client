package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(RedlistCategory.class)
public abstract class RedlistCategory_ {

    public static volatile SingularAttribute<RedlistCategory,String> redlistCode;

    public static volatile SingularAttribute<RedlistCategory,Long> id;

    public static volatile SingularAttribute<RedlistCategory,String> label;
}
