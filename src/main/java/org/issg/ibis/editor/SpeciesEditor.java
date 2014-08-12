package org.issg.ibis.editor;

import java.util.List;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.QSpecies;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.Species_;
import org.issg.ibis.editor.selector.AbstractSelector;
import org.issg.ibis.editor.view.DefaultEditorView;
import org.issg.ibis.editor.view.TwinPanelEditorView;
import org.issg.upload.AbstractUploader.ProcessingCompleteEvent;
import org.issg.upload.AbstractUploader.ProcessingCompleteListener;
import org.issg.upload.SpeciesUploader;
import org.issg.upload.ThreatSummaryUploader;
import org.jrc.edit.Dao;
import org.jrc.edit.EditorController;
import org.jrc.edit.JpaFieldFactory;
import org.vaadin.addons.form.field.FieldGroup;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

public class SpeciesEditor extends TwinPanelEditorView<Species> implements View {

	private EditorController<Species> ec;
	private SpeciesToolBar toolBar;

	@Inject
    public SpeciesEditor(Dao dao, RoleManager roleManager, SpeciesSummaryEditor sse, SpeciesImpactEditor sie, SpeciesLocationEditor sle) {

        ec = new EditorController<Species>(Species.class, dao, roleManager);

    	toolBar = new SpeciesToolBar(ec);
    	toolBar.addButton("Impacts", sie);
    	toolBar.addButton("Locations", sle);
    	toolBar.addButton("Summaries", sse);
        
         
        JpaFieldFactory<Species> ff = ec.getFf();
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

        TwinPanelEditorView<Species> view = new TwinPanelEditorView<Species>();
        ec.init(this);
        
        AbstractSelector<Species> selector = new AbstractSelector<Species>(dao, QSpecies.species1, ec.getContainer());
        selector.setSizeFull();
        selector.addColumns("name", "commonName");
        ec.setSelectionComponent(selector);
        this.setSelectionComponent(selector);
        
        SpeciesUploader uploader = new SpeciesUploader(dao);
        view.addComponent(uploader);
        uploader.addProcessingCompleteListener(new ProcessingCompleteListener() {
            @Override
            public void processingComplete(ProcessingCompleteEvent p) {
                
            }
        });
        
        ThreatSummaryUploader tsu = new ThreatSummaryUploader(dao);
        view.addComponent(tsu);
        
        tsu.addProcessingCompleteListener(new ProcessingCompleteListener() {
            
            @Override
            public void processingComplete(ProcessingCompleteEvent p) {
                List<?> res = p.getResults();
                for (Object obj : res) {
                    System.out.println(obj);
//                    table.
                }
            }
        });

    }
    
    @Override
    public void buildForm(List<FieldGroup<Species>> fields) {

		formLayout.addComponent(toolBar);

    	super.buildForm(fields);
    }

	@Override
	public void enter(ViewChangeEvent event) {
	}

}
