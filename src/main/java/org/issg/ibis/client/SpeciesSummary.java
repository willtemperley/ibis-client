package org.issg.ibis.client;

import org.issg.ibis.display.SimpleHtmlHeader;
import org.issg.ibis.display.SimpleHtmlLabel;
import org.issg.ibis.display.SimplePanel;
import org.issg.ibis.domain.Species;

import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;

public class SpeciesSummary extends Panel {

    private SimpleHtmlHeader speciesName = new SimpleHtmlHeader();
    private SimpleHtmlLabel speciesImage = new SimpleHtmlLabel();
    private SimpleHtmlLabel threatSummary = new SimpleHtmlLabel();
    private SimpleHtmlLabel redlistStatus = new SimpleHtmlLabel();
    private SimpleHtmlLabel commonName = new SimpleHtmlLabel();

    public SpeciesSummary() {

        SimplePanel root = new SimplePanel();
        setContent(root);
        
//        speciesImage.setContentMode(ContentMode.HTML);
//        threatSummary.setContentMode(ContentMode.HTML);
//        speciesName.setContentMode(ContentMode.HTML);
//        redlistStatus.setContentMode(ContentMode.HTML);
//        commonName.setContentMode(ContentMode.HTML);
        
        root.addComponent(speciesName);
        
//        root.addComponent(commonName);
        root.addComponent(speciesImage);
        
        root.addComponent(new SimpleHtmlHeader("Conservation status"));
        
//        root.addComponent(threatSummary);
        root.addComponent(redlistStatus);

    }

    public void setSpecies(Species sp) {
        // speciesInfo.setSpecies(sp, imgUrl);
        String img = ArkiveV1Search.getSpeciesImage(sp.getName());
        
        speciesName.setValue(sp.getScientificName());
        speciesImage.setValue(img);
        
        commonName.setValue(sp.getCommonName());
        
        threatSummary.setValue(sp.getThreatSummary());
        
        String redlistImgUrl = String.format("<img src='/ibis-client/VAADIN/themes/dashboard/redlist/240px-Status_iucn3.1_%s.svg.png'/>", sp.getRedlistCategory().getRedlistCode());
        redlistStatus.setValue(redlistImgUrl);
        
    }

}
