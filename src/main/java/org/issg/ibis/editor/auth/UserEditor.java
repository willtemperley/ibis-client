package org.issg.ibis.editor.auth;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.editor.basic.BasicTwinPanelEditor;
import org.jrc.edit.Dao;
import org.vaadin.addons.auth.domain.QRole;
import org.vaadin.addons.auth.domain.Role;

import com.google.inject.Inject;

public class UserEditor extends BasicTwinPanelEditor<Role> {

    @Inject
    public UserEditor(Dao dao, RoleManager roleManager) {

    	super(Role.class, dao, roleManager);

    	QRole entityPath = QRole.role;

		ff.addQField(entityPath.groups);
		ff.addQField(entityPath.permissions);

		ff.addQField(entityPath.lastName);
		ff.addQField(entityPath.firstName);

		ff.addQField(entityPath.canLogin);
		ff.addQField(entityPath.isSuperUser);
		ff.addQField(entityPath.email);


        build(entityPath, entityPath.firstName, entityPath.email);
    }

}
