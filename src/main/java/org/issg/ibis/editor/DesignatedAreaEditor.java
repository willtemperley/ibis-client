package org.issg.ibis.editor;

import java.util.List;

import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.LocationType;
import org.issg.ibis.domain.Location_;
import org.issg.upload.AbstractUploader.ProcessingCompleteEvent;
import org.issg.upload.AbstractUploader.ProcessingCompleteListener;
import org.issg.upload.ThreatSummaryUploader;
import org.jrc.form.editor.ui.SimpleTwinPanelEditor;
import org.jrc.persist.Dao;

import com.google.inject.Inject;
import com.vaadin.data.util.filter.Compare;

public class DesignatedAreaEditor extends
        SimpleTwinPanelEditor<Location> {

    private LocationType locationType;

    @Inject
    public DesignatedAreaEditor(Dao dao) {
        super(Location.class, dao);
        
        this.locationType = dao.find(LocationType.class, "Designated Area");

        getTable().addColumns(Location_.name, Location_.designatedAreaType);

//         filterPanel.addFilterField(Location_.redlistCategory);
//         filterPanel.addFilterField(TableDescription_.schema);
         
        ff.addField(Location_.name);
        ff.addField(Location_.country);

        ff.addField(Location_.latitude);
        ff.addField(Location_.longitude);

        ff.addField(Location_.prefix);
        ff.addField(Location_.identifier);

        ff.addField(Location_.url);

        ff.addField(Location_.area);

        ff.addField(Location_.designatedAreaType);
        ff.addTextArea(Location_.comments);
        
        containerManager.getContainer().addContainerFilter(new Compare.Equal(Location_.locationType.getName(), locationType));

        init();
        
//        LocationUploader uploader = new LocationUploader(dao);
//        theView.addSelectionComponent(uploader);
//        uploader.addProcessingCompleteListener(new ProcessingCompleteListener() {
//            @Override
//            public void processingComplete(ProcessingCompleteEvent p) {
//                DesignatedAreaEditor.this.containerManager.refresh();
////                List<?> res = p.getResults();
////                for (Object obj : res) {
////                    System.out.println(obj);
////                }
//                
//            }
//        });
        
        ThreatSummaryUploader tsu = new ThreatSummaryUploader(dao);
        theView.addSelectionComponent(tsu);
        
        tsu.addProcessingCompleteListener(new ProcessingCompleteListener() {
            
            @Override
            public void processingComplete(ProcessingCompleteEvent p) {
                List<?> res = p.getResults();
                for (Object obj : res) {
//                    System.out.println(obj);
                }
                DesignatedAreaEditor.this.containerManager.refresh();
                
            }
        });

    }
    
    @Override
    protected void doPreCommit(Location obj) {
        obj.setLocationType(locationType);
        super.doPreCommit(obj);
    }

}
