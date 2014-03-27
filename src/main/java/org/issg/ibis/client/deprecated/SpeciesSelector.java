package org.issg.ibis.client.deprecated;

import it.jrc.form.editor.EntityTable;

import org.issg.ibis.ViewModule;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.Species_;
import org.jrc.persist.ContainerManager;
import org.jrc.persist.Dao;
import org.vaadin.addons.form.field.filter.FilterField;
import org.vaadin.addons.form.field.filter.FilterPanel;

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
@Deprecated
public class SpeciesSelector extends CssLayout {
    
    final ContainerManager<Species> containerManager;
    private FilterField<Species, String> speciesNameFilter;
    
    @Inject
    public SpeciesSelector(Dao dao) {
        
        containerManager = new ContainerManager<Species>(dao, Species.class);
        
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
