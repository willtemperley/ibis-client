package org.issg.ibis.editor.basic;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.domain.ImpactMechanism;
import org.issg.ibis.domain.QImpactMechanism;
import org.jrc.edit.Dao;

import com.google.inject.Inject;

public class ImpactMechanismEditor extends BasicTwinPanelEditor<ImpactMechanism> {

    @Inject
    public ImpactMechanismEditor(Dao dao, RoleManager roleManager) {

    	super(ImpactMechanism.class, dao, roleManager);

        QImpactMechanism entityPath = QImpactMechanism.impactMechanism;
		ff.addQField(entityPath.label);

        build(entityPath);
    }

}
