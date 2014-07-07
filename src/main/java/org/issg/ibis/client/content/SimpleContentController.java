package org.issg.ibis.client.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.issg.ibis.domain.Content;
import org.issg.ibis.domain.ContentType;
import org.issg.ibis.domain.Reference;
import org.issg.ibis.perspective.species.SpeciesSummaryController;
import org.jrc.ui.HtmlHeader;
import org.jrc.ui.HtmlLabel;
import org.jrc.ui.SimplePanel;

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
    
    protected void setReferences(Set<Reference> refs) {
    	for (Reference reference : refs) {

            HtmlLabel content = new HtmlLabel(reference.getContent());

            panel.addComponent(content);
		}
    }
}
