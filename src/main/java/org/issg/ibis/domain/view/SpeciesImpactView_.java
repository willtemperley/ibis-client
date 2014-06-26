package org.issg.ibis.domain.view;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(SpeciesImpactView.class)
public abstract class SpeciesImpactView_ {

    public static volatile SingularAttribute<SpeciesImpactView, String> nativeSpecies;

    public static volatile SingularAttribute<SpeciesImpactView, String> country;

    public static volatile SingularAttribute<SpeciesImpactView, String> impactMechanism;

    public static volatile SingularAttribute<SpeciesImpactView, String> biologicalStatus;

    public static volatile SingularAttribute<SpeciesImpactView, String> location;

    public static volatile SingularAttribute<SpeciesImpactView, Long> id;

    public static volatile SingularAttribute<SpeciesImpactView, String> invasiveSpecies;

	public static volatile SingularAttribute<SpeciesImpactView, String> impactOutcome;

	public static volatile SingularAttribute<SpeciesImpactView, String> invasiveCommonName;
	
}
