package org.issg.ibis.editor;

import java.util.List;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.QLocation;
import org.issg.ibis.editor.view.TwinPanelEditorView;
import org.issg.ibis.responsive.LocationSearch2;
import org.issg.upload.AbstractUploader.ProcessingCompleteEvent;
import org.issg.upload.AbstractUploader.ProcessingCompleteListener;
import org.jrc.edit.Dao;
import org.jrc.edit.EditorController;
import org.jrc.edit.JpaFieldFactory;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class LocationEditor extends TwinPanelEditorView<Location> implements View {

	private EditorController<Location> ec;

    @Inject
    public LocationEditor(Dao dao, RoleManager roleManager) {

        ec = new EditorController<Location>(Location.class, dao, roleManager);

//      getTable().addColumns(Location_.name, Location_.islandType, Location_.latitude, Location_.longitude);
//
//       filterPanel.addFilterField(Location_.name);
//       filterPanel.addFilterField(Location_.country);
//       filterPanel.addFilterField(TableDescription_.schema);
         
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
        
        LocationSearch2 locationSearch = new LocationSearch2(dao, ec.getContainer());
        ec.setSelectionComponent(locationSearch);

	    locationSearch.setCaption("Select location to edit");
	    
        this.setSelectionComponent(locationSearch);

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
