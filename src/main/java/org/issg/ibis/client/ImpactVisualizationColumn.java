package org.issg.ibis.client;

import org.issg.ibis.domain.SpeciesImpact;

import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

class ImpactVisualizationColumn implements Table.ColumnGenerator {

    public Component generateCell(Table source, Object itemId,
            Object columnId) {
        JPAContainerItem<?> item = (JPAContainerItem<?>) source.getItem(itemId);
        final SpeciesImpact si = (SpeciesImpact) item.getEntity();
        return new ImpactVisualization(si);
    }
}