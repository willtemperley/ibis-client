package org.biopama.ibis.editor.basic;

import org.biopama.edit.Dao;
import org.biopama.ibis.auth.RoleManager;
import org.issg.ibis.domain.ImpactMechanism;
import org.issg.ibis.domain.QImpactMechanism;

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
