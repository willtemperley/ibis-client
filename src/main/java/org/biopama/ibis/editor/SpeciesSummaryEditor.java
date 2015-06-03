package org.biopama.ibis.editor;

import java.util.List;

import org.biopama.edit.Dao;
import org.biopama.edit.EditorController;
import org.biopama.edit.JpaFieldFactory;
import org.biopama.ibis.auth.RoleManager;
import org.biopama.ibis.editor.selector.AbstractSelector;
import org.biopama.ibis.editor.view.DefaultEditorView;
import org.biopama.ibis.editor.view.TwinPanelEditorView;
import org.biopama.ibis.upload.AbstractUploader.ProcessingCompleteEvent;
import org.biopama.ibis.upload.AbstractUploader.ProcessingCompleteListener;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.QSpeciesSummary;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesSummary;

import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.inject.Inject;
import com.mysema.query.jpa.impl.JPAQuery;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class SpeciesSummaryEditor extends TwinPanelEditorView<SpeciesSummary> implements View, ISpeciesEditor {

    private AbstractSelector<SpeciesSummary> selector;
	private Dao dao;
	private Species species;
	
	@Override
	public String getCaption() {
		return "Edit species summaries";
	}

	@Inject
    public SpeciesSummaryEditor(Dao dao, RoleManager roleManager) {

        EditorController<SpeciesSummary> ec = new EditorController<SpeciesSummary>(SpeciesSummary.class, dao, roleManager) {
        	@Override
        	protected void doPreCommit(SpeciesSummary entity) {
        		entity.setSpecies(species);
        	}
        };
        this.dao = dao;
         
        JpaFieldFactory<SpeciesSummary> ff = ec.getFf();
        ff.addQField(QSpeciesSummary.speciesSummary.contentType);
        ff.addQRichTextArea(QSpeciesSummary.speciesSummary.content).setWidth("600px");

        ec.init(this);
        
        selector = new AbstractSelector<SpeciesSummary>(dao, QSpeciesSummary.speciesSummary, ec.getContainer());
        selector.setSizeFull();
        selector.addColumns("contentType");
        ec.setSelectionComponent(selector);
        this.setSelectionComponent(selector);
        

    }

	@Override
	public void enter(ViewChangeEvent event) {

	}

	public void setSpecies(Species sp) {
		
		JPAQuery q = new JPAQuery(dao.get());
		q = q.from(QSpeciesSummary.speciesSummary).where(QSpeciesSummary.speciesSummary.species.eq(sp));
		
		this.species = sp;

		List<SpeciesSummary> results = q.list(QSpeciesSummary.speciesSummary);
		selector.setBeans(results);
	}

}
