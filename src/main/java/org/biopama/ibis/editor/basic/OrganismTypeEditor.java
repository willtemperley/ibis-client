package org.biopama.ibis.editor.basic;

import org.biopama.edit.Dao;
import org.biopama.ibis.auth.RoleManager;
import org.issg.ibis.domain.OrganismType;
import org.issg.ibis.domain.QOrganismType;

import com.google.inject.Inject;

public class OrganismTypeEditor extends BasicTwinPanelEditor<OrganismType> {

    @Inject
    public OrganismTypeEditor(Dao dao, RoleManager roleManager) {

    	super(OrganismType.class, dao, roleManager);

        QOrganismType cc = QOrganismType.organismType;
		ff.addQField(cc.label);

        build(cc);
    }

}
