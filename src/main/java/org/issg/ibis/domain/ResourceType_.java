package org.issg.ibis.domain;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ResourceType.class)
public abstract class ResourceType_ {

    public static volatile SingularAttribute<ResourceType, String> id;

    public static volatile SingularAttribute<ResourceType, String> name;
}
