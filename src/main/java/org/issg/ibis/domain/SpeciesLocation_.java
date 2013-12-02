package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(SpeciesLocation.class)
public abstract class SpeciesLocation_ {

    public static volatile SingularAttribute<SpeciesLocation, Location> location;

    public static volatile SingularAttribute<SpeciesLocation, BiologicalStatus> biologicalStatus;

    public static volatile SingularAttribute<SpeciesLocation, Long> id;

    public static volatile SingularAttribute<SpeciesLocation, Species> species;
}
