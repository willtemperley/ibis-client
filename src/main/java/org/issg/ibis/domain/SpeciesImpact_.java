package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.vividsolutions.jts.geom.Polygon;

@StaticMetamodel(SpeciesImpact.class)
public abstract class SpeciesImpact_ {

    public static volatile SingularAttribute<SpeciesImpact, Species> threatenedSpecies;

    public static volatile SingularAttribute<SpeciesImpact, Species> invasiveSpecies;

    public static volatile SingularAttribute<SpeciesImpact, ImpactMechanism> impactMechanism;

    public static volatile SingularAttribute<SpeciesImpact, ImpactOutcome> impactOutcome;

    public static volatile SingularAttribute<SpeciesImpact, Long> id;
    
    public static volatile SingularAttribute<SpeciesImpact, Polygon> envelope;

    public static volatile SingularAttribute<SpeciesImpact, Location> location;

}
