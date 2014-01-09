package org.issg.ibis.domain.view;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.issg.ibis.domain.Species;

@StaticMetamodel(ResourceDescriptionSpecies.class)
public abstract class ResourceDescriptionSpecies_ {

    public static volatile SingularAttribute<ResourceDescriptionSpecies, String> id;

    public static volatile SingularAttribute<ResourceDescriptionSpecies, ResourceDescription> resourceDescription;
    
    public static volatile SingularAttribute<ResourceDescriptionSpecies, Species> species;
    
}
