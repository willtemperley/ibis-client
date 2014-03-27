package org.issg.ibis.client.deprecated;

import it.jrc.form.editor.EntityTable;

import org.issg.ibis.ViewModule;
import org.jrc.persist.ContainerManager;
import org.jrc.persist.Dao;
import org.jrc.persist.adminunits.Country;
import org.jrc.persist.adminunits.Country_;
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
public class CountrySelector extends CssLayout {
    
    final ContainerManager<Country> containerManager;
    private FilterField<Country, String> speciesNameFilter;
    
    @Inject
    public CountrySelector(Dao dao) {
        
        containerManager = new ContainerManager<Country>(dao, Country.class);
        
        EntityTable<Country> table = getTable(dao);
        table.addColumns(Country_.name);
        
        JPAContainer<Country> container = containerManager.getContainer();
        FilterPanel<Country> fp = new FilterPanel<Country>(container, dao);
        speciesNameFilter = fp.addFilterField(Country_.name);
//        fp.addFilterField(Country_.);
        
        this.addComponent(fp);
        this.addComponent(table);
    }
    
    private EntityTable<Country> getTable(final Dao dao) {


        final EntityTable<Country> table = new EntityTable<Country>(containerManager.getContainer());

        table.setPageLength(20);

        table.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                
               Long id = (Long) event.getProperty().getValue();

               Country species = containerManager.findEntity(id);
                
               getUI().getNavigator().navigateTo(ViewModule.getCountryLink(species));
            }
        });
        
        
        table.setWidth("500px");

        return table;
    }
    
    public void setSearchText(String searchText) {
        Like filter = new Like(Country_.name.getName(), "%" + searchText + "%");
        JPAContainer<Country> container = containerManager.getContainer();
        container.addContainerFilter(filter);
        speciesNameFilter.getField().setValue(searchText);
    }

}
