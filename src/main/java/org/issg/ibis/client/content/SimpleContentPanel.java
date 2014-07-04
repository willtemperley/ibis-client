package org.issg.ibis.client.content;

import java.util.ArrayList;
import java.util.List;

import org.issg.ibis.domain.Content;
import org.issg.ibis.domain.ContentType;
import org.jrc.ui.HtmlHeader;
import org.jrc.ui.HtmlLabel;
import org.jrc.ui.SimplePanel;

import com.vaadin.ui.Component;

public class SimpleContentPanel  {

    protected List<Component> contentComponents = new ArrayList<Component>();

    protected SimplePanel panel;
    
    public SimpleContentPanel(SimplePanel panel) {
        this.panel = panel;
    }

    protected void addContent(List<? extends Content> ss) {
        for (Content speciesSummary : ss) {
            ContentType ct = speciesSummary.getContentType();
            HtmlHeader header = new HtmlHeader(ct.getName());
            panel.addComponent(header);
    
            HtmlLabel content = new HtmlLabel();
            content.addStyleName("content");
            content.setValue(speciesSummary.getContent());
            panel.addComponent(content);
    
            contentComponents.add(header);
            contentComponents.add(content);
        }
    }
    
}
