package org.issg.ibis.qdsl.experimental;

import java.util.ArrayList;
import java.util.List;

import org.issg.ibis.client.content.SimpleContentController;
import org.issg.ibis.domain.Content;
import org.issg.ibis.domain.ContentType;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesSummary;
import org.issg.ibis.webservices.ArkiveV1Search;
import org.jrc.ui.HtmlHeader;
import org.jrc.ui.HtmlLabel;
import org.jrc.ui.SimplePanel;

import com.google.inject.Inject;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Table;

public class SpeciesSummaryView extends SimplePanel {

    protected List<Component> contentComponents = new ArrayList<Component>();
    private HtmlHeader speciesName = new HtmlHeader();
    private HtmlLabel speciesImage = new HtmlLabel();
    private HtmlLabel redlistStatus = new HtmlLabel();
	private ArkiveV1Search arkiveSearch;

    @Inject
    public SpeciesSummaryView(ArkiveV1Search arkiveSearch) {
    	
    	this.arkiveSearch = arkiveSearch;

        addComponent(speciesName);
        speciesName.addStyleName("header-large");

        addComponent(speciesImage);

        addComponent(new HtmlHeader("Conservation status"));
        addComponent(redlistStatus);

    }

    public void setSpecies(Species sp) {

        String img = arkiveSearch.getSpeciesImage(sp.getName());

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
            HtmlHeader header = new HtmlHeader(ct.getName());
            addComponent(header);
    
            HtmlLabel content = new HtmlLabel();
            content.setValue(speciesSummary.getContent());
            addComponent(content);
    
            contentComponents.add(header);
            contentComponents.add(content);
        }
    }

}
