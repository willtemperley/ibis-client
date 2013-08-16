package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(DesignatedArea.class)
public abstract class DesignatedArea_ {

    public static volatile SingularAttribute<DesignatedArea,String> areaid;

    public static volatile SingularAttribute<DesignatedArea,Long> idcountry;

    public static volatile SingularAttribute<DesignatedArea,Island> island;

    public static volatile SingularAttribute<DesignatedArea,String> siteName;

    public static volatile SingularAttribute<DesignatedArea,DesignatedAreaType> designatedAreaType;

    public static volatile SingularAttribute<DesignatedArea,Double> totalsize;

    public static volatile SingularAttribute<DesignatedArea,Long> latitude;

    public static volatile SingularAttribute<DesignatedArea,Long> longitude;

    public static volatile SingularAttribute<DesignatedArea,String> url;

    public static volatile SingularAttribute<DesignatedArea,String> comments;

    public static volatile SingularAttribute<DesignatedArea,Long> id;
}
