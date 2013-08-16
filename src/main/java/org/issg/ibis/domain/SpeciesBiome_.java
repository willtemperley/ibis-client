package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(SpeciesBiome.class)
public abstract class SpeciesBiome_ {

    public static volatile SingularAttribute<SpeciesBiome,Species> species;

    public static volatile SingularAttribute<SpeciesBiome,Biome> biome;

    public static volatile SingularAttribute<SpeciesBiome,Long> id;
}
