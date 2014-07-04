package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(SpeciesImpact.class)
public abstract class SpeciesImpact_ {

    public static volatile SingularAttribute<SpeciesImpact,Species> nativeSpecies;

    public static volatile SingularAttribute<SpeciesImpact,Species> invasiveSpecies;

    public static volatile SingularAttribute<SpeciesImpact,ImpactMechanism> impactMechanism;

    public static volatile SingularAttribute<SpeciesImpact,ImpactOutcome> impactOutcome;

    public static volatile SingularAttribute<SpeciesImpact,Long> id;

    public static volatile SingularAttribute<SpeciesImpact,Location> location;

    public static volatile SingularAttribute<SpeciesImpact,UploadLog> uploadLog;

    public static volatile SingularAttribute<SpeciesImpact,Reference> reference;
}
