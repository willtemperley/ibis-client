package org.issg.ibis.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(SpeciesThreatSummary.class)
public abstract class SpeciesThreatSummary_ {

    public static volatile SingularAttribute<SpeciesThreatSummary,Species> species;

    public static volatile SingularAttribute<SpeciesThreatSummary,Long> idcountry;

    public static volatile SingularAttribute<SpeciesThreatSummary,String> threatsummarytitle;

    public static volatile SingularAttribute<SpeciesThreatSummary,String> threatsummaryshort;

    public static volatile SingularAttribute<SpeciesThreatSummary,String> threatSummary;

    public static volatile SingularAttribute<SpeciesThreatSummary,String> speciesManagement;

    public static volatile SingularAttribute<SpeciesThreatSummary,String> conservationOutcomes;

    public static volatile SingularAttribute<SpeciesThreatSummary,String> threatsummarycode;

    public static volatile SingularAttribute<SpeciesThreatSummary,Long> id;
}
