package org.issg.ibis.a;

import java.util.Collection;
import java.util.List;

import javax.persistence.metamodel.PluralAttribute;

import org.issg.ibis.client.pending.CriteriaQueryManager;
import org.issg.ibis.domain.view.ResourceDescription;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;

public class CriteriaFilterPanel<T> extends FormLayout {

    private static String DEFAULT_WIDTH = "300px";

    private CriteriaQueryManager<T> queryManager;

    private BeanItemContainer<T> container;

    /**
     * Remains hidden until a field is added.
     * 
     * @param esiContainer
     * 
     * @param bic
     * @param dao
     */
    public CriteriaFilterPanel(BeanItemContainer<T> bic,
            CriteriaQueryManager<T> cqm) {
        this.queryManager = cqm;
        this.container = bic;
    }

    public void doQuery() {

        List<ResourceDescription> rds = queryManager.getResourceDescriptions();
        container.removeAllItems();
        container.addAll((Collection<? extends T>) rds);

    }

    /**
     * Adds a filter field
     * 
     * @param prop
     */
    public <E, C extends Collection<E>> void addFilterField(
            final PluralAttribute<T, C, E> attr, Field<?> field) {

        field.setWidth(DEFAULT_WIDTH);

        field.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {

                E val = (E) event.getProperty().getValue();
                //null is an option
                queryManager.addExistsPredicate(attr, val);
                doQuery();

            }
        });

        this.addComponent(field);
    }

}
