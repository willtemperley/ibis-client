package org.issg.ibis;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

public class HeaderView extends CssLayout implements View {
    
    
    @Inject
    public HeaderView(AccountOptionView menuView) {
//        this.addComponent(menuView);
        this.setStyleName("header");
        
        Label label = new Label();
        label.setContentMode(ContentMode.HTML);
//        label.setValue("<div class='ibis'>IBIS</div><div class='ibis-text'>ISLAND BIODIVERSITY<br/><i>&amp; INVASIVE SPECIES</i></div>");
        
        
        String text = 
                "<div><div class='ibis'>ibis</div>" +
        		"<div class='ibis-text'>island biodiversity<br/>&amp; invasive species</div></div>" +
                        
        		"<div class='logo-container'><div class='issg-logo'></div><div class='ec-logo'></div><div class='iucn-logo'></div></div>";
        
        label.setValue(text);
        
        addComponent(label);
    }
    

    @Override
    public void enter(ViewChangeEvent event) {
        
    }

}
