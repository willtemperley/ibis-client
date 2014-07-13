package org.issg.ibis.editor;

import java.util.List;

import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.LocationType;
import org.issg.ibis.domain.Location_;
import org.issg.upload.AbstractUploader.ProcessingCompleteEvent;
import org.issg.upload.AbstractUploader.ProcessingCompleteListener;
import org.issg.upload.ThreatSummaryUploader;
import org.jrc.edit.Dao;
import org.jrc.edit.EditorController;
import org.vaadin.addons.form.view.DefaultEditorView;

import com.google.inject.Inject;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class LocationEditor extends EditorController<Location> implements View {

    private LocationType locationType;

    @Inject
    public LocationEditor(Dao dao) {
        super(Location.class, dao);

        this.locationType = dao.find(LocationType.class, "Island");

//        getTable().addColumns(Location_.name, Location_.islandType, Location_.latitude, Location_.longitude);
//
//       filterPanel.addFilterField(Location_.name);
//       filterPanel.addFilterField(Location_.country);
//         filterPanel.addFilterField(TableDescription_.schema);
         
        ff.addField(Location_.name);
        ff.addField(Location_.islandGroup);
        ff.addField(Location_.country);
        ff.addField(Location_.islandType);

        ff.addField(Location_.latitude);
        ff.addField(Location_.longitude);

        ff.addField(Location_.prefix);
        ff.addField(Location_.identifier);

        ff.addField(Location_.url);

        ff.addField(Location_.area);

        ff.addTextArea(Location_.comments);
        
        containerManager.getContainer().addContainerFilter(new Compare.Equal(Location_.locationType.getName(), locationType));

        DefaultEditorView<Location> view = new DefaultEditorView<Location>();

        ThreatSummaryUploader tsu = new ThreatSummaryUploader(dao);
        view.addComponent(tsu);

		init(view);
        
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
                LocationEditor.this.containerManager.refresh();
                
            }
        });

    }

    @Override
    protected void doPreCommit(Location obj) {
        obj.setLocationType(locationType);
        super.doPreCommit(obj);
    }

	@Override
	public void enter(ViewChangeEvent event) {
		
	}
}
