package org.issg.ibis.client;

import org.issg.ibis.ViewModule;
import org.issg.ibis.display.SimplePanel;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.Species_;
import org.jrc.form.editor.EditorPanel;
import org.jrc.form.editor.EntityTable;
import org.jrc.form.filter.FilterPanel;
import org.jrc.persist.ContainerManager;
import org.jrc.persist.Dao;

import com.google.inject.Inject;
import com.vaadin.data.Property;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class SpeciesSelector extends Panel {
    
    final ContainerManager<Species> containerManager;
    
    @Inject
    public SpeciesSelector(Dao dao) {
        
        containerManager = new ContainerManager<Species>(dao, Species.class);
        
        SimplePanel panel = new SimplePanel();
        setContent(panel);
        
        EntityTable<Species> table = getTable(dao);
        table.addColumns(Species_.name, Species_.redlistCategory);
        
        FilterPanel<Species> fp = new FilterPanel<Species>(containerManager.getContainer(), dao);
        fp.addFilterField(Species_.name);
        fp.addFilterField(Species_.redlistCategory);
        
        
        panel.addComponent(fp);
        panel.addComponent(table);
        
    }
    
    private EntityTable<Species> getTable(Dao dao) {

        
        final EntityTable<Species> table = new EntityTable<Species>(containerManager.getContainer());

//        table.setHeight("100%");
        table.setPageLength(20);

        table.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                
               Long id = (Long) event.getProperty().getValue();

               Species species = containerManager.findEntity(id);
                
               getUI().getNavigator().navigateTo(ViewModule.getSpeciesLink(species));
            }
        });

        return table;
    }

}
