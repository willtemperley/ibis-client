package org.issg.ibis.editor;

import java.util.List;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.domain.QSpeciesLocation;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesLocation;
import org.issg.ibis.domain.SpeciesLocation_;
import org.issg.ibis.editor.selector.AbstractSelector;
import org.issg.ibis.editor.view.TwinPanelEditorView;
import org.jrc.edit.Dao;
import org.jrc.edit.EditorController;
import org.jrc.edit.JpaFieldFactory;

import com.google.inject.Inject;
import com.mysema.query.jpa.impl.JPAQuery;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class SpeciesLocationEditor extends TwinPanelEditorView<SpeciesLocation> implements View, ISpeciesEditor {

    private Dao dao;
	private AbstractSelector<SpeciesLocation> selector;
	private Species species;

	@Override
	public String getCaption() {
		return "Edit Species Locations";
	}

	@Inject
    public SpeciesLocationEditor(Dao dao, RoleManager roleManager) {

        EditorController<SpeciesLocation> ec = new EditorController<SpeciesLocation>(SpeciesLocation.class, dao, roleManager) {
        	@Override
        	protected void doPreCommit(SpeciesLocation entity) {
        		entity.setSpecies(species);
        	}
        };

        this.dao = dao;
         
        JpaFieldFactory<SpeciesLocation> ff = ec.getFf();
        ff.addField(SpeciesLocation_.biologicalStatus);
        ff.addField(SpeciesLocation_.location);
        
		ec.init(this);
        
        selector = new AbstractSelector<SpeciesLocation>(dao, QSpeciesLocation.speciesLocation, ec.getContainer());
        selector.setSizeFull();
        selector.addColumns("location");
        selector.addColumns("biologicalStatus");
        ec.setSelectionComponent(selector);
        this.setSelectionComponent(selector);
    }

	@Override
	public void enter(ViewChangeEvent event) {
		
	}

	public void setSpecies(Species sp) {
		
		JPAQuery q = new JPAQuery(dao.get());
		q = q.from(QSpeciesLocation.speciesLocation).where(QSpeciesLocation.speciesLocation.species.eq(sp));
		
		this.species = sp;

		List<SpeciesLocation> results = q.list(QSpeciesLocation.speciesLocation);
		selector.setBeans(results);
	}
}
