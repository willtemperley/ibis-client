package org.issg.ibis.editor;

import java.util.List;

import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.SpeciesImpact_;
import org.issg.upload.AbstractUploader.ProcessingCompleteEvent;
import org.issg.upload.AbstractUploader.ProcessingCompleteListener;
import org.issg.upload.SpeciesImpactUploader;
import org.jrc.form.editor.ui.SimpleTwinPanelEditor;
import org.jrc.persist.Dao;

import com.google.inject.Inject;

public class SpeciesImpactEditor extends
        SimpleTwinPanelEditor<SpeciesImpact> {

    @Inject
    public SpeciesImpactEditor(Dao dao) {
        super(SpeciesImpact.class, dao);

        getTable().addColumns(
                SpeciesImpact_.nativeSpecies,
                SpeciesImpact_.invasiveSpecies,
                SpeciesImpact_.impactMechanism, 
                SpeciesImpact_.impactOutcome);

//        filterPanel.addFilterField(SpeciesImpact_.threatenedSpecies);
//         filterPanel.addFilterField(TableDescription_.schema);
         
        ff.addField(SpeciesImpact_.nativeSpecies);
        ff.addField(SpeciesImpact_.invasiveSpecies);

        ff.addField(SpeciesImpact_.impactMechanism);
        ff.addField(SpeciesImpact_.impactOutcome);
        
        init();
        
        SpeciesImpactUploader uploader = new SpeciesImpactUploader(dao);
        theView.addSelectionComponent(uploader);
        uploader.addProcessingCompleteListener(new ProcessingCompleteListener() {
            @Override
            public void processingComplete(ProcessingCompleteEvent p) {
                List<?> res = p.getResults();
                for (Object obj : res) {
                    System.out.println(obj);
//                    table.
                }
                SpeciesImpactEditor.this.containerManager.refresh();
                
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
