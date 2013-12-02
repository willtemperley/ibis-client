package org.issg.ibis.client;

import java.util.List;

import org.issg.ibis.client.content.SimpleContentPanel;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesSummary;
import org.issg.ibis.ws.ArkiveV1Search;
import org.jrc.ui.SimpleHtmlHeader;
import org.jrc.ui.SimpleHtmlLabel;
import org.jrc.ui.SimplePanel;

import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

public class SpeciesSummaryView extends SimpleContentPanel {

    private SimpleHtmlHeader speciesName = new SimpleHtmlHeader();
    private SimpleHtmlLabel speciesImage = new SimpleHtmlLabel();
    private SimpleHtmlLabel redlistStatus = new SimpleHtmlLabel();

    //FIXME: commonName
    private SimpleHtmlLabel commonName = new SimpleHtmlLabel();

    
    public SpeciesSummaryView(SimplePanel panel, Table t) {

        super(panel);

        panel.addComponent(speciesName);
        speciesName.addStyleName("header-large");

        // root.addComponent(commonName);
        panel.addComponent(speciesImage);

        panel.addComponent(new SimpleHtmlHeader("Conservation status"));
        panel.addComponent(redlistStatus);

        panel.addComponent(new SimpleHtmlHeader("Known impacts"));
        panel.addComponent(t);

    }

    public void setSpecies(Species sp) {

        String img = ArkiveV1Search.getSpeciesImage(sp.getName());

        speciesName.setValue(sp.getScientificName());
        speciesImage.setValue(img);
        
        
        //Clear content
        for (Component cc : contentComponents) {
            panel.removeComponent(cc);
        }
        contentComponents.clear();
        
        
        List<SpeciesSummary> ss = sp.getSpeciesSummaries();

        addContent(ss);

//        commonName.setValue(sp.getCommonName());

        if (sp.getRedlistCategory() != null) {
            String redlistImgUrl = String
                    .format("<img src='/ibis-client/VAADIN/themes/dashboard/redlist/240px-Status_iucn3.1_%s.svg.png'/>",
                            sp.getRedlistCategory().getRedlistCode());
            redlistStatus.setValue(redlistImgUrl);
        }

    }

    public void setLocation(Location location) {
        
    }

}
