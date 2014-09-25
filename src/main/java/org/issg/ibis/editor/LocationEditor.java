package org.issg.ibis.editor;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.QLocation;
import org.issg.ibis.editor.selector.AbstractSelector;
import org.issg.ibis.editor.view.TwinPanelEditorView;
import org.issg.ibis.responsive.LocationSearch2;
import org.jrc.edit.Dao;
import org.jrc.edit.EditorController;
import org.jrc.edit.JpaFieldFactory;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

public class LocationEditor extends TwinPanelEditorView<Location> implements View {

	private EditorController<Location> ec;
	
	private LocationSearchWindow searchWindow;

    @Inject
    public LocationEditor(Dao dao, RoleManager roleManager) {

        ec = new EditorController<Location>(Location.class, dao, roleManager);
    	searchWindow = new LocationSearchWindow(ec, dao);

    	setCreateListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				searchWindow.center();
				searchWindow.setModal(true);
				UI.getCurrent().addWindow(searchWindow);
			}
		});

         
        JpaFieldFactory<Location> ff = ec.getFf();
        QLocation loc = QLocation.location;
		ff.addQField(loc.name);
        ff.addQField(loc.country);
        ff.addQField(loc.locationType);

        ff.addQField(loc.islandGroup);

        ff.addQField(loc.latitude);
        ff.addQField(loc.longitude);

        ff.addQField(loc.prefix);
        ff.addQField(loc.identifier);

        ff.addQField(loc.url);
        ff.addQField(loc.area);

        ff.addQTextArea(loc.comments);
        ec.init(this);
        
//        LocationSearch2 locationSearch = new LocationSearch2(dao, ec.getContainer());

        AbstractSelector<Location> selector = new AbstractSelector<Location>(dao, QLocation.location, ec.getContainer());
		selector.addColumns("name", "locationType");
        ec.setSelectionComponent(selector);

        this.setSelectionComponent(selector);

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
}
