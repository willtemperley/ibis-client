package org.issg.ibis.editor;

import java.util.List;

import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.SpeciesImpact_;
import org.issg.upload.AbstractUploader.ProcessingCompleteEvent;
import org.issg.upload.AbstractUploader.ProcessingCompleteListener;
import org.issg.upload.SpeciesImpactUploader;
import org.jrc.edit.Dao;
import org.jrc.edit.EditorController;
import org.vaadin.addons.form.view.DefaultEditorView;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class SpeciesImpactEditor extends
        EditorController<SpeciesImpact> implements View {

    @Inject
    public SpeciesImpactEditor(Dao dao) {
        super(SpeciesImpact.class, dao);

//        getTable().addColumns(
//                SpeciesImpact_.nativeSpecies,
//                SpeciesImpact_.invasiveSpecies,
//                SpeciesImpact_.impactMechanism, 
//                SpeciesImpact_.impactOutcome);

//        filterPanel.addFilterField(SpeciesImpact_.threatenedSpecies);
//         filterPanel.addFilterField(TableDescription_.schema);
         
        ff.addField(SpeciesImpact_.nativeSpecies);
        ff.addField(SpeciesImpact_.invasiveSpecies);

        ff.addField(SpeciesImpact_.impactMechanism);
        ff.addField(SpeciesImpact_.impactOutcome);
        
        addFieldGroup("");

        DefaultEditorView<SpeciesImpact> view = new DefaultEditorView<SpeciesImpact>();

		init(view);
        
        SpeciesImpactUploader uploader = new SpeciesImpactUploader(dao);
        view.addComponent(uploader);
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

	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}
