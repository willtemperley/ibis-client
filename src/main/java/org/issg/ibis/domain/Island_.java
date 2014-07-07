package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Island.class)
public abstract class Island_ {

    public static volatile SingularAttribute<Island,Country> country;

    public static volatile SingularAttribute<Island,IslandType> islandType;

    public static volatile SingularAttribute<Island,String> islandName;

    public static volatile SingularAttribute<Island,String> islandGroup;

    public static volatile SingularAttribute<Island,Double> latitudeDec;

    public static volatile SingularAttribute<Island,Double> longitudeDec;

    public static volatile SingularAttribute<Island,String> latitudeDeg;

    public static volatile SingularAttribute<Island,String> longitudeDeg;

    public static volatile SingularAttribute<Island,String> islandNotes;

    public static volatile SingularAttribute<Island,Long> id;
}
