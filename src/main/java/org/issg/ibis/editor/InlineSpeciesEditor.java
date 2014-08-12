package org.issg.ibis.editor;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.Species_;
import org.issg.ibis.editor.view.DefaultEditorView;
import org.jrc.edit.Dao;
import org.jrc.edit.EditorController;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class InlineSpeciesEditor extends EditorController<Species>{

    public InlineSpeciesEditor(Class<Species> clazz, Dao dao, RoleManager roleManager) {
        super(clazz, dao, roleManager);
        
        ff.addField(Species_.name);

        ff.addField(Species_.kingdom);
        ff.addField(Species_.phylum);
        ff.addField(Species_.clazz);
        ff.addField(Species_.order);
        ff.addField(Species_.family);
        ff.addField(Species_.genus);
        ff.addField(Species_.species);
        
        ff.addField(Species_.synonyms);
        ff.addField(Species_.organismType);
        ff.addField(Species_.gisdLink);
        ff.addField(Species_.commonName);
        ff.addField(Species_.authority);
        ff.addField(Species_.redlistCategory);
        ff.addField(Species_.redlistId);
        ff.addField(Species_.biomes);
        ff.addField(Species_.references);
        
        addFieldGroup("");
        
        InlineEditSpeciesView v = new InlineEditSpeciesView();
        v.getButton().addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				
				Species x = getEntity();
				x.populateFromJson();
				doUpdate(x);
			}
		});

        init(v);
    }

    protected Species commit(Species entity) {
        return (Species) dao.merge(entity);
    }

}
