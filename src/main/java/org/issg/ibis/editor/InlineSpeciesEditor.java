package org.issg.ibis.editor;

import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.Species_;
import org.jrc.persist.Dao;
import org.vaadin.addons.form.controller.EditorController;
import org.vaadin.addons.form.view.DefaultEditorView;

public class InlineSpeciesEditor extends EditorController<Species>{

    public InlineSpeciesEditor(Class<Species> clazz, Dao dao) {
        super(clazz, dao);
        
        ff.addField(Species_.name);
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
        
        init(new DefaultEditorView<Species>());
    }


    
    protected Species commit(Species entity) {
        return (Species) dao.merge(entity);
    }


}
