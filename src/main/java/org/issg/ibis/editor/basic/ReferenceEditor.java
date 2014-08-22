package org.issg.ibis.editor.basic;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.domain.Reference;
import org.issg.ibis.domain.QReference;
import org.jrc.edit.Dao;

import com.google.inject.Inject;

public class ReferenceEditor extends BasicTwinPanelEditor<Reference> {

    @Inject
    public ReferenceEditor(Dao dao, RoleManager roleManager) {

    	super(Reference.class, dao, roleManager);

        QReference entityPath = QReference.reference;
        ff.addQField(entityPath.label);
        ff.addQField(entityPath.url);
		ff.addQRichTextArea(entityPath.content);

        build(entityPath);
        
        selector.addColumns("id", "label");
    }

}
