package org.issg.ibis.client;

import org.issg.ibis.ViewModule;
import org.issg.ibis.client.pending.CriteriaQueryDelegate;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.Species_;
import org.jrc.form.editor.EntityTable;
import org.jrc.form.filter.FilterField;
import org.jrc.form.filter.FilterPanel;
import org.jrc.persist.CriteriaContainerManager;
import org.jrc.persist.Dao;

import com.google.inject.Inject;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Like;
import com.vaadin.ui.CssLayout;

/**
 * 
 * @author Will Temperley
 *
 */
public class SpeciesSelector extends CssLayout {
    
    final CriteriaContainerManager<Species> containerManager;
    private FilterField<Species, String> speciesNameFilter;
    
    @Inject
    public SpeciesSelector(Dao dao) {
        
        containerManager = new CriteriaContainerManager<Species>(dao, Species.class, new CriteriaQueryDelegate(dao));
        
        EntityTable<Species> table = getTable(dao);
        table.addColumns(Species_.name, Species_.redlistCategory);
        
        JPAContainer<Species> container = containerManager.getContainer();
        FilterPanel<Species> fp = new FilterPanel<Species>(container, dao);
        speciesNameFilter = fp.addFilterField(Species_.name);
        fp.addFilterField(Species_.redlistCategory);
        
        this.addComponent(fp);
        this.addComponent(table);
    }
    
    private EntityTable<Species> getTable(final Dao dao) {


        final EntityTable<Species> table = new EntityTable<Species>(containerManager.getContainer());

        table.setPageLength(20);

        table.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                
               Long id = (Long) event.getProperty().getValue();

               Species species = containerManager.findEntity(id);
                
               getUI().getNavigator().navigateTo(ViewModule.getSpeciesLink(species));
            }
        });
        
        
        table.setWidth("500px");

        return table;
    }
    
    public void setSearchText(String searchText) {
        Like filter = new Like(Species_.name.getName(), "%" + searchText + "%");
        JPAContainer<Species> container = containerManager.getContainer();
        container.addContainerFilter(filter);
        speciesNameFilter.getField().setValue(searchText);
    }

}
