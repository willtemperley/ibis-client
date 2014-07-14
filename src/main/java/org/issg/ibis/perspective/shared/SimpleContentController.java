package org.issg.ibis.perspective.shared;

import java.util.ArrayList;
import java.util.List;

import org.issg.ibis.domain.Content;
import org.issg.ibis.domain.ContentType;
import org.jrc.ui.HtmlHeader;
import org.jrc.ui.HtmlLabel;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

public class SimpleContentController  {

    private List<Component> contentComponents = new ArrayList<Component>();

    protected CssLayout panel;
    
    public SimpleContentController(CssLayout panel) {
        this.panel = panel;
    }

    public void setContent(List<? extends Content> ss) {
    	for (Component content : contentComponents) {
    		panel.removeComponent(content);
		}
    	contentComponents.clear();
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
