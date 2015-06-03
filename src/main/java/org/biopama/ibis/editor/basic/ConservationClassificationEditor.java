package org.biopama.ibis.editor.basic;

import org.biopama.edit.Dao;
import org.biopama.edit.EditorController;
import org.biopama.edit.JpaFieldFactory;
import org.biopama.ibis.auth.RoleManager;
import org.biopama.ibis.editor.selector.AbstractSelector;
import org.biopama.ibis.editor.view.TwinPanelEditorView;
import org.issg.ibis.domain.ConservationClassification;
import org.issg.ibis.domain.QConservationClassification;

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
