package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.jrc.persist.adminunits.Country;

@StaticMetamodel(FacetedSearch.class)
public abstract class FacetedSearch_ {

    public static volatile SingularAttribute<FacetedSearch, Country> country;

    public static volatile SingularAttribute<FacetedSearch, String> id;

    public static volatile SingularAttribute<FacetedSearch, String> designatedAreaType;

    public static volatile SingularAttribute<FacetedSearch, String> name;

    public static volatile SingularAttribute<FacetedSearch, ResourceType> resourceType;
    
    
}
