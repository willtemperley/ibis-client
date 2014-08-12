package org.issg.ibis.editor;

import java.util.List;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Location_;
import org.issg.ibis.editor.view.TwinPanelEditorView;
import org.issg.ibis.responsive.LocationSearch2;
import org.issg.upload.AbstractUploader.ProcessingCompleteEvent;
import org.issg.upload.AbstractUploader.ProcessingCompleteListener;
import org.issg.upload.ThreatSummaryUploader;
import org.jrc.edit.Dao;
import org.jrc.edit.EditorController;
import org.jrc.edit.JpaFieldFactory;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class LocationEditor extends TwinPanelEditorView<Location> implements View {

	private EditorController<Location> ec;
	private Dao dao;

    @Inject
    public LocationEditor(Dao dao, RoleManager roleManager) {

        ec = new EditorController<Location>(Location.class, dao, roleManager);
        this.dao = dao;

//      getTable().addColumns(Location_.name, Location_.islandType, Location_.latitude, Location_.longitude);
//
//       filterPanel.addFilterField(Location_.name);
//       filterPanel.addFilterField(Location_.country);
//       filterPanel.addFilterField(TableDescription_.schema);
         
        JpaFieldFactory<Location> ff = ec.getFf();
		ff.addField(Location_.name);
        ff.addField(Location_.country);
        ff.addField(Location_.locationType);

        ff.addField(Location_.islandGroup);
        ff.addField(Location_.islandType);

        ff.addField(Location_.latitude);
        ff.addField(Location_.longitude);

        ff.addField(Location_.prefix);
        ff.addField(Location_.identifier);

        ff.addField(Location_.url);
        ff.addField(Location_.area);

        ff.addTextArea(Location_.comments);
        ec.init(this);
        
        LocationSearch2 locationSearch = new LocationSearch2(dao, ec.getContainer());
        ec.setSelectionComponent(locationSearch);

	    locationSearch.setCaption("Select location to edit");
	    
        this.setSelectionComponent(locationSearch);

        ThreatSummaryUploader tsu = new ThreatSummaryUploader(dao);
//        view.addComponent(tsu);
        
//        LocationUploader uploader = new LocationUploader(dao);
//        theView.addSelectionComponent(uploader);
//        uploader.addProcessingCompleteListener(new ProcessingCompleteListener() {
//            @Override
//            public void processingComplete(ProcessingCompleteEvent p) {
//                IslandEditor.this.containerManager.refresh();
////                List<?> res = p.getResults();
////                for (Object obj : res) {
////                    System.out.println(obj);
////                }
//                
//            }
//        });
        
        
        tsu.addProcessingCompleteListener(new ProcessingCompleteListener() {
            
            @Override
            public void processingComplete(ProcessingCompleteEvent p) {
                List<?> res = p.getResults();
                for (Object obj : res) {
                    System.out.println(obj);
//                    table.
                }
//                LocationEditor.this.containerManager.refresh();
                
            }
        });

    }


	@Override
	public void enter(ViewChangeEvent event) {
		String s = event.getParameters();

		if (!ec.hasReadPermission()) {
			throw new RuntimeException("Unauthorized access.");
		}

		if (!s.isEmpty()) {
			Long l = Long.valueOf(s);
			ec.doUpdate(dao.find(Location.class, l));
		}
	}
}
