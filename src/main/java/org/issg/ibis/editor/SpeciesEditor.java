package org.issg.ibis.editor;

import java.util.List;

import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.Species_;
import org.issg.upload.AbstractUploader.ProcessingCompleteEvent;
import org.issg.upload.AbstractUploader.ProcessingCompleteListener;
import org.issg.upload.SpeciesUploader;
import org.issg.upload.ThreatSummaryUploader;
import org.jrc.form.editor.ui.SimpleTwinPanelEditor;
import org.jrc.persist.Dao;

import com.google.inject.Inject;

public class SpeciesEditor extends
        SimpleTwinPanelEditor<Species> {

    @Inject
    public SpeciesEditor(Dao dao) {
        super(Species.class, dao);

        getTable().addColumns(Species_.name, Species_.redlistCategory);

         filterPanel.addFilterField(Species_.redlistCategory);
//         filterPanel.addFilterField(TableDescription_.schema);
         
        ff.addField(Species_.name);
        ff.addField(Species_.genus);
        ff.addField(Species_.species);
        ff.addField(Species_.uri);
        ff.addField(Species_.synonyms);
        ff.addField(Species_.organismType);
        ff.addField(Species_.gisdLink);
        ff.addField(Species_.commonName);
        ff.addField(Species_.authority);
        ff.addField(Species_.redlistCategory);
        ff.addField(Species_.redlistId);
        ff.addField(Species_.biomes);
        ff.addField(Species_.references);

        init();
        
        SpeciesUploader uploader = new SpeciesUploader(dao);
        theView.addSelectionComponent(uploader);
        uploader.addProcessingCompleteListener(new ProcessingCompleteListener() {
            @Override
            public void processingComplete(ProcessingCompleteEvent p) {
                SpeciesEditor.this.containerManager.refresh();
//                List<?> res = p.getResults();
//                for (Object obj : res) {
//                    System.out.println(obj);
//                }
                
            }
        });
        
        ThreatSummaryUploader tsu = new ThreatSummaryUploader(dao);
        theView.addSelectionComponent(tsu);
        
        tsu.addProcessingCompleteListener(new ProcessingCompleteListener() {
            
            @Override
            public void processingComplete(ProcessingCompleteEvent p) {
                List<?> res = p.getResults();
                for (Object obj : res) {
                    System.out.println(obj);
//                    table.
                }
                SpeciesEditor.this.containerManager.refresh();
                
            }
        });

    }
    // private OrderedCollectionTable<ColumnDescription> getFtff() {
    //
    // FormTableFieldFactory<ColumnDescription> ftff = new
    // FormTableFieldFactory<ColumnDescription>(
    // dao);
    // ftff.addField(ColumnDescription_.name);
    // ftff.addField(ColumnDescription_.description);
    // OrderedCollectionTable<ColumnDescription> tab = new
    // OrderedCollectionTable<ColumnDescription>(ColumnDescription.class, ftff);
    //
    // return tab;
    // }

}
