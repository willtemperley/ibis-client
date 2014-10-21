package org.issg.ibis.editor.basic;

import org.issg.ibis.ViewModule;
import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.domain.ConservationClassification;
import org.issg.ibis.editor.selector.AbstractSelector;
import org.issg.ibis.editor.view.TwinPanelEditorView;
import org.jrc.edit.Dao;
import org.jrc.edit.EditorController;
import org.jrc.edit.JpaFieldFactory;

import com.mysema.query.types.Path;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.StringPath;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.UI;

public  class BasicTwinPanelEditor<T> extends TwinPanelEditorView<T> implements View {
	
	protected EditorController<T> ec;
	protected AbstractSelector<T> selector;
	protected JpaFieldFactory<T> ff;
	private Dao dao;

	public BasicTwinPanelEditor(Class<T> clazz, Dao dao, RoleManager roleManager) {
        ec = new EditorController<T>(clazz, dao, roleManager);
        ff = ec.getFf();
        this.dao = dao;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		String s = event.getParameters();

		if (!ec.hasReadPermission()) {
			UI.getCurrent().getNavigator().navigateTo(ViewModule.UNAUTHORIZED);
			return;
		}

		if (!s.isEmpty()) {
			Long l = Long.valueOf(s);
			ec.doUpdateById(l);
		}
	}

	protected void build(EntityPathBase<T> ebp, Path<?> ... firstName) {

        selector = new AbstractSelector<T>(dao, ebp, ec.getContainer());
        
        Object[] visibleColumns = new Object[firstName.length];
        for (int i = 0; i < firstName.length; i++) {
        	visibleColumns[i] = firstName[i].getMetadata().getName();
		}

        selector.setVisibleColumns(visibleColumns);

        ec.setSelectionComponent(selector);
        this.setSelectionComponent(selector);
		ec.init(this);
	}

}
