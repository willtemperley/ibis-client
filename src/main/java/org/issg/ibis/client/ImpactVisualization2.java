package org.issg.ibis.client;

import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.SpeciesImpact;
import org.jrc.ui.SimpleHtmlLabel;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class ImpactVisualization2 extends Panel {
    
    public ImpactVisualization2(SpeciesImpact speciesImpact) {
        
        VerticalLayout hl = new VerticalLayout();

        hl.addComponent(new SimpleHtmlLabel(speciesImpact.getInvasiveSpecies().getScientificName()));
        hl.addComponent(new SimpleHtmlLabel(speciesImpact.getImpactMechanism().getLabel() + " causing " + speciesImpact.getImpactOutcome().getLabel()));
        hl.addComponent(new SimpleHtmlLabel(getLink(speciesImpact.getLocation())));
        
        setContent(hl);
    }

    private String getLink(Location loc) {
        StringBuilder sb = new StringBuilder("<a href='#!");
        sb.append("Location/");
        sb.append(loc.getId());
        sb.append("'>");
        sb.append(loc.getName());
        sb.append("</a>");
        return sb.toString();
    }
}
