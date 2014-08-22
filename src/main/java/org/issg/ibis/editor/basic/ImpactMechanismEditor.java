package org.issg.ibis.editor.basic;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.domain.OrganismType;
import org.issg.ibis.domain.QOrganismType;
import org.jrc.edit.Dao;

import com.google.inject.Inject;

public class ImpactMechanismEditor extends BasicTwinPanelEditor<OrganismType> {

    @Inject
    public ImpactMechanismEditor(Dao dao, RoleManager roleManager) {

    	super(OrganismType.class, dao, roleManager);

        QOrganismType entityPath = QOrganismType.organismType;
		ff.addQField(entityPath.label);

        build(entityPath);
    }

}
