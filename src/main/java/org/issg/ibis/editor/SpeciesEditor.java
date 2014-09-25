package org.issg.ibis.editor;

import java.util.ArrayList;
import java.util.List;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.domain.QSpecies;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.json.CommonName;
import org.issg.ibis.editor.selector.AbstractSelector;
import org.issg.ibis.editor.view.TwinPanelEditorView;
import org.issg.ibis.webservices.gbif.GbifSpeciesClient;
import org.issg.upload.AbstractUploader.ProcessingCompleteEvent;
import org.issg.upload.AbstractUploader.ProcessingCompleteListener;
import org.issg.upload.SpeciesUploader;
import org.jrc.edit.Dao;
import org.jrc.edit.EditorController;
import org.jrc.edit.JpaFieldFactory;
import org.vaadin.addons.form.field.FieldGroup;
import org.vaadin.maddon.fields.MTextField;
import org.vaadin.maddon.fields.MValueChangeEvent;
import org.vaadin.maddon.fields.MValueChangeListener;
import org.vaadin.maddon.fields.TypedSelect;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;

public class SpeciesEditor extends TwinPanelEditorView<Species> implements View {

	private EditorController<Species> ec;
	private SpeciesToolBar toolBar;
	private SpeciesSearchWindow gbifSearchWindow;
	private GbifSpeciesClient gbifSpeciesClient = new GbifSpeciesClient();
	private CommonNameCombo commonNameCombo;

	@Inject
    public SpeciesEditor(Dao dao, RoleManager roleManager, SpeciesSummaryEditor sse, SpeciesImpactEditor sie, SpeciesLocationEditor sle) {

        commonNameCombo = new CommonNameCombo().withPixelWidth(300);

        ec = new EditorController<Species>(Species.class, dao, roleManager) {
        	@Override
        	protected void preUpdate(Species entity) {
        		commonNameCombo.removeAllItems();

        		List<CommonName> x = gbifSpeciesClient.getVernacularNames(entity.getUri());
        		if (x == null) {
        			return;
				}
        		for (CommonName c : x) {
        			commonNameCombo.addItem(c.getVernacularName());
        			if (c.getPreferred()) {
        				commonNameCombo.setValue(c.getVernacularName());
					}
        		}
        	}
        };

    	toolBar = new SpeciesToolBar(ec);
    	toolBar.addButton("Impacts", sie);
    	toolBar.addButton("Locations", sle);
    	toolBar.addButton("Summaries", sse);
    	
		gbifSearchWindow = new SpeciesSearchWindow(ec);

    	setCreateListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				gbifSearchWindow.center();
				gbifSearchWindow.setModal(true);
				UI.getCurrent().addWindow(gbifSearchWindow);
			}
		});
    	
    	QSpecies sp = QSpecies.species;
        JpaFieldFactory<Species> ff = ec.getFf();

        ff.addQField(sp.name);
        ff.addQField(sp.uri);
        
        ff.addQField(sp.kingdom);
        ff.addQField(sp.phylum);
        ff.addQField(sp.clazz).setCaption("Class");;
        ff.addQField(sp.order);
        ff.addQField(sp.genus);

        ff.addQField(sp.organismType);
        ff.addQField(sp.gisdLink).setCaption("GISD link");
        ff.addQField(sp.commonName, commonNameCombo);
        ff.addQField(sp.authority);
        ff.addQField(sp.redlistId);
        ff.addQField(sp.conservationClassification);
        ff.addQField(sp.biomes).setCaption("Environment / System");
        ff.addQField(sp.references).setWidth(50, Unit.EM);

        TwinPanelEditorView<Species> view = new TwinPanelEditorView<Species>();
        ec.init(this);
        
        AbstractSelector<Species> selector = new AbstractSelector<Species>(dao, QSpecies.species, ec.getContainer());
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
        
    }
    
    @Override
    public void buildForm(List<FieldGroup<Species>> fields) {

		formLayout.addComponent(toolBar);
    	super.buildForm(fields);
    }

	@Override
	public void enter(ViewChangeEvent event) {
		
		String strId = event.getParameters();
		if (strId.isEmpty()) {
			return;
		}

		try {
			Long id = Long.valueOf(strId);
			ec.doUpdateById(id);

		} catch(NumberFormatException e) {
			e.printStackTrace();
		}

	}

}
