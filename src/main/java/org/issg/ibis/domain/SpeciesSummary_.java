package org.issg.ibis.domain;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(SpeciesSummary.class)
public abstract class SpeciesSummary_ {

    public static volatile SingularAttribute<SpeciesSummary, ContentType> contentType;

    public static volatile SingularAttribute<SpeciesSummary, String> content;

    public static volatile SingularAttribute<SpeciesSummary, Long> id;

    public static volatile SingularAttribute<SpeciesSummary, Species> species;
}
