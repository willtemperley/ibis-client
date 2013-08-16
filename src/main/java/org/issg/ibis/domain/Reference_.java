package org.issg.ibis.domain;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Reference.class)
public abstract class Reference_ {

    public static volatile SingularAttribute<Reference,String> referenceHeader;

    public static volatile SingularAttribute<Reference,String> referenceContext;

    public static volatile SingularAttribute<Reference,String> referenceFull;

    public static volatile SingularAttribute<Reference,String> referenceCode;

    public static volatile SingularAttribute<Reference,Long> id;

    public static volatile SetAttribute<Reference,Species> speciess;
}
