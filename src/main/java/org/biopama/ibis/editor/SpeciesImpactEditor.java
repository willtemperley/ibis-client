package org.biopama.ibis.editor;

import java.util.List;

import org.biopama.edit.Dao;
import org.biopama.edit.EditorController;
import org.biopama.edit.JpaFieldFactory;
import org.biopama.ibis.auth.RoleManager;
import org.biopama.ibis.editor.selector.AbstractSelector;
import org.biopama.ibis.editor.view.TwinPanelEditorView;
import org.issg.ibis.domain.QSpeciesImpact;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;

import com.google.inject.Inject;
import com.mysema.query.jpa.impl.JPAQuery;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class SpeciesImpactEditor extends TwinPanelEditorView<SpeciesImpact> implements View, ISpeciesEditor {

    private Dao dao;
	private AbstractSelector<SpeciesImpact> selector;
	private Species species;

	@Override
	public String getCaption() {
		return "Edit impacts";
	}

	@Inject
    public SpeciesImpactEditor(Dao dao, RoleManager roleManager) {

        EditorController<SpeciesImpact> ec = new EditorController<SpeciesImpact>(SpeciesImpact.class, dao, roleManager) {
        	@Override
        	protected void doPreCommit(SpeciesImpact entity) {
        		entity.setNativeSpecies(species);
        	}
        };

        this.dao = dao;
         
        JpaFieldFactory<SpeciesImpact> ff = ec.getFf();
        QSpeciesImpact si = QSpeciesImpact.speciesImpact;
        ff.addQField(si.invasiveSpecies);
        ff.addQField(si.impactMechanism);
//        ff.addQField(si.impactOutcome);
        ff.addQField(si.location);
        ff.addQField(si.reference);
        
		ec.init(this);
        
        selector = new AbstractSelector<SpeciesImpact>(dao, QSpeciesImpact.speciesImpact, ec.getContainer());
        selector.setSizeFull();
        selector.addColumns("invasiveSpecies");
        ec.setSelectionComponent(selector);
        this.setSelectionComponent(selector);
    }

	@Override
	public void enter(ViewChangeEvent event) {
		
	}

	public void setSpecies(Species sp) {
		
		JPAQuery q = new JPAQuery(dao.get());
		q = q.from(QSpeciesImpact.speciesImpact).where(QSpeciesImpact.speciesImpact.nativeSpecies.eq(sp));
		
		this.species = sp;

		List<SpeciesImpact> results = q.list(QSpeciesImpact.speciesImpact);
		selector.setBeans(results);
	}
}
