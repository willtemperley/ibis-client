package org.issg.ibis.client;

import org.issg.ibis.domain.SpeciesImpact;

import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

class ImpactVisualizationColumn2 implements Table.ColumnGenerator {

    public Component generateCell(Table source, Object itemId,
            Object columnId) {
        BeanItem<?> item = (BeanItem<?>) source.getItem(itemId);
        final SpeciesImpact si = (SpeciesImpact) item.getBean();
        return new ImpactVisualization2(si);
    }
}