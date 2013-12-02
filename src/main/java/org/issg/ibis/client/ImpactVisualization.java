package org.issg.ibis.client;

import org.issg.ibis.ViewModule;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.jrc.ui.SimpleHtmlLabel;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class ImpactVisualization extends Panel {
    
    public ImpactVisualization(SpeciesImpact speciesImpact) {
        
        VerticalLayout hl = new VerticalLayout();
        

        hl.addComponent(new SimpleHtmlLabel(getLink(speciesImpact.getThreatenedSpecies())));

        hl.addComponent(new SimpleHtmlLabel(speciesImpact.getInvasiveSpecies().getScientificName()));
        
        setContent(hl);
    }

    private String getLink(Species species) {
        StringBuilder sb = new StringBuilder("<a href='#!");
        sb.append(ViewModule.getSpeciesLink(species));
        sb.append("'>");
        sb.append(species.getScientificName());
        sb.append("</a>");
        return sb.toString();
    }
}
