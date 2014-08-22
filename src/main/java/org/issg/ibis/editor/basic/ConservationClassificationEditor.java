package org.issg.ibis.editor.basic;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.domain.ConservationClassification;
import org.issg.ibis.domain.QConservationClassification;
import org.issg.ibis.editor.selector.AbstractSelector;
import org.issg.ibis.editor.view.TwinPanelEditorView;
import org.jrc.edit.Dao;
import org.jrc.edit.EditorController;
import org.jrc.edit.JpaFieldFactory;

import com.google.inject.Inject;
import com.mysema.query.types.PathType;
import com.mysema.query.types.path.StringPath;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class ConservationClassificationEditor extends BasicTwinPanelEditor<ConservationClassification> {

    @Inject
    public ConservationClassificationEditor(Dao dao, RoleManager roleManager) {

    	super(ConservationClassification.class, dao, roleManager);

        QConservationClassification cc = QConservationClassification.conservationClassification;
		ff.addQField(cc.name);
        ff.addQField(cc.abbreviation);

        build(cc);
    }

}