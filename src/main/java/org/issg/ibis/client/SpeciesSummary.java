package org.issg.ibis.client;

import org.issg.ibis.client.js.SpeciesInfo;
import org.issg.ibis.domain.Species;

import com.vaadin.ui.Accordion;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class SpeciesSummary extends Panel {
    
    private SpeciesInfo speciesInfo;
    private Label summary1;

    public SpeciesSummary() {
        
        Accordion accordion = new Accordion();
        
        
        speciesInfo = new SpeciesInfo();
        
        summary1 = new Label();
        accordion.addTab(speciesInfo, "Species");
        accordion.addTab(summary1, "Threat summary");
        
        setContent(accordion);
    }

    public void setSpecies(Species sp, String imgUrl) {
        speciesInfo.setSpecies(sp, imgUrl);
        summary1.setCaption(sp.getThreatSummary());
    }

}
