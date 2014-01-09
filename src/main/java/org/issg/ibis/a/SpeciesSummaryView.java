package org.issg.ibis.a;

import java.util.ArrayList;
import java.util.List;

import org.issg.ibis.client.content.SimpleContentPanel;
import org.issg.ibis.domain.Content;
import org.issg.ibis.domain.ContentType;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesSummary;
import org.issg.ibis.ws.ArkiveV1Search;
import org.jrc.ui.SimpleHtmlHeader;
import org.jrc.ui.SimpleHtmlLabel;
import org.jrc.ui.SimplePanel;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Table;

public class SpeciesSummaryView extends SimplePanel {

    protected List<Component> contentComponents = new ArrayList<Component>();
    private SimpleHtmlHeader speciesName = new SimpleHtmlHeader();
    private SimpleHtmlLabel speciesImage = new SimpleHtmlLabel();
    private SimpleHtmlLabel redlistStatus = new SimpleHtmlLabel();

    public SpeciesSummaryView() {

        addComponent(speciesName);
        speciesName.addStyleName("header-large");

        addComponent(speciesImage);

        addComponent(new SimpleHtmlHeader("Conservation status"));
        addComponent(redlistStatus);

    }

    public void setSpecies(Species sp) {

        String img = ArkiveV1Search.getSpeciesImage(sp.getName());

        speciesName.setValue(sp.getScientificName());
        speciesImage.setValue(img);
        
        //Clear content
        for (Component cc : contentComponents) {
            removeComponent(cc);
        }
        contentComponents.clear();
        
        
        List<SpeciesSummary> ss = sp.getSpeciesSummaries();

        addContent(ss);

        if (sp.getRedlistCategory() != null) {
            String redlistImgUrl = String
                    .format("<img src='/ibis-client/VAADIN/themes/dashboard/redlist/240px-Status_iucn3.1_%s.svg.png'/>",
                            sp.getRedlistCategory().getRedlistCode());
            redlistStatus.setValue(redlistImgUrl);
        }

    }

    protected void addContent(List<? extends Content> ss) {
        for (Content speciesSummary : ss) {
            ContentType ct = speciesSummary.getContentType();
            SimpleHtmlHeader header = new SimpleHtmlHeader(ct.getName());
            addComponent(header);
    
            SimpleHtmlLabel content = new SimpleHtmlLabel();
            content.setValue(speciesSummary.getContent());
            addComponent(content);
    
            contentComponents.add(header);
            contentComponents.add(content);
        }
    }

}
