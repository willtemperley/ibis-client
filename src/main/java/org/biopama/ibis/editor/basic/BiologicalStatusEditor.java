package org.biopama.ibis.editor.basic;

import org.biopama.edit.Dao;
import org.biopama.ibis.auth.RoleManager;
import org.issg.ibis.domain.BiologicalStatus;
import org.issg.ibis.domain.QBiologicalStatus;

import com.google.inject.Inject;

public class BiologicalStatusEditor extends BasicTwinPanelEditor<BiologicalStatus> {

    @Inject
    public BiologicalStatusEditor(Dao dao, RoleManager roleManager) {

    	super(BiologicalStatus.class, dao, roleManager);

        QBiologicalStatus entityPath = QBiologicalStatus.biologicalStatus;
		ff.addQField(entityPath.label);
		ff.addQField(entityPath.isInvasive);

        build(entityPath, entityPath.label);
    }

}
