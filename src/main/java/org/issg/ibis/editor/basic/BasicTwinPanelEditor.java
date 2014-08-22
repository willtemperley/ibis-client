package org.issg.ibis.editor.basic;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.domain.ConservationClassification;
import org.issg.ibis.editor.selector.AbstractSelector;
import org.issg.ibis.editor.view.TwinPanelEditorView;
import org.jrc.edit.Dao;
import org.jrc.edit.EditorController;
import org.jrc.edit.JpaFieldFactory;

import com.mysema.query.types.path.EntityPathBase;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

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
			throw new RuntimeException("Unauthorized access.");
		}

		if (!s.isEmpty()) {
			Long l = Long.valueOf(s);
			ec.doUpdateById(l);
		}
	}

	protected void build(EntityPathBase<T> ebp) {
        selector = new AbstractSelector<T>(dao, ebp, ec.getContainer());
        ec.setSelectionComponent(selector);
        this.setSelectionComponent(selector);
		ec.init(this);
	}

}