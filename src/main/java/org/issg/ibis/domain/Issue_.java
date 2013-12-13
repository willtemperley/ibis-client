package org.issg.ibis.domain;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.jrc.auth.domain.Role;

@StaticMetamodel(Issue.class)
public abstract class Issue_ {

    public static volatile SingularAttribute<Issue, String> title;

    public static volatile SingularAttribute<Issue, String> content;

    public static volatile SingularAttribute<Issue, Long> id;

    public static volatile SingularAttribute<Issue, String> links;

    public static volatile SingularAttribute<Issue, Role> role;
}
