package org.issg.ibis.client;

import org.issg.ibis.ViewModule;
import org.issg.ibis.display.SimplePanel;
import org.issg.ibis.domain.RedlistCategory;
import org.issg.ibis.domain.RedlistCategory_;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.Species_;
import org.jrc.form.editor.EntityTable;
import org.jrc.form.filter.FilterPanel;
import org.jrc.persist.ContainerManager;
import org.jrc.persist.Dao;

import com.google.inject.Inject;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Not;
import com.vaadin.ui.Panel;

public class SpeciesSelector extends Panel {
    
    final ContainerManager<Species> containerManager;
    
    @Inject
    public SpeciesSelector(Dao dao) {
        
        containerManager = new ContainerManager<Species>(dao, Species.class);
        
        SimplePanel panel = new SimplePanel();
        setContent(panel);
        
        EntityTable<Species> table = getTable(dao);
        table.addColumns(Species_.name, Species_.redlistCategory);
        
        JPAContainer<Species> container = containerManager.getContainer();
        FilterPanel<Species> fp = new FilterPanel<Species>(container, dao);
        fp.addFilterField(Species_.name);
        fp.addFilterField(Species_.redlistCategory);
        
        RedlistCategory rlc = dao.findByProxyId(RedlistCategory_.label, "Invasive");
        container.addContainerFilter(new Not(new Compare.Equal(Species_.redlistCategory.getName(), rlc)));
        
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
