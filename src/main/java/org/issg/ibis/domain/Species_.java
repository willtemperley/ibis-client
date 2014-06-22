package org.issg.ibis.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Species.class)
public abstract class Species_ {

    public static volatile SingularAttribute<Species, String> name;

    public static volatile SingularAttribute<Species, String> uri;

//    public static volatile SingularAttribute<Species, Taxon> genus;

    public static volatile SingularAttribute<Species, String> kingdom;
    public static volatile SingularAttribute<Species, String> phylum;
    public static volatile SingularAttribute<Species, String> clazz;
    public static volatile SingularAttribute<Species, String> order;
    public static volatile SingularAttribute<Species, String> family;
    public static volatile SingularAttribute<Species, String> genus;

    public static volatile SingularAttribute<Species, Long> nubKey;

    public static volatile SingularAttribute<Species, String> species;

    public static volatile SingularAttribute<Species, String> synonyms;

    public static volatile SingularAttribute<Species, OrganismType> organismType;

    public static volatile SingularAttribute<Species, String> gisdLink;

    public static volatile SingularAttribute<Species, String> commonName;

    public static volatile SingularAttribute<Species, String> authority;

    public static volatile SingularAttribute<Species, String> link;

    public static volatile SingularAttribute<Species, Long> id;

    public static volatile SingularAttribute<Species, RedlistCategory> redlistCategory;

    public static volatile SingularAttribute<Species, String> imageUrl;
    
    public static volatile SingularAttribute<Species, Long> redlistId;

    public static volatile SetAttribute<Species, Biome> biomes;

    public static volatile SetAttribute<Species, Reference> references;

    public static volatile ListAttribute<Species, SpeciesSummary> speciesSummaries;

}
