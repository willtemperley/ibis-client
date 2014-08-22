package org.issg.ibis.editor.basic;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.domain.ImpactOutcome;
import org.issg.ibis.domain.QImpactOutcome;
import org.jrc.edit.Dao;

import com.google.inject.Inject;

public class ImpactOutcomeEditor extends BasicTwinPanelEditor<ImpactOutcome> {

    @Inject
    public ImpactOutcomeEditor(Dao dao, RoleManager roleManager) {

    	super(ImpactOutcome.class, dao, roleManager);

        QImpactOutcome entityPath = QImpactOutcome.impactOutcome;
		ff.addQField(entityPath.label);

        build(entityPath);
    }

}
