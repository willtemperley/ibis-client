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
        label.setValue("<h1>IBIS</h1><div>Island Biodiversity and Invasive Species</div>");
        addComponent(label);
    }
    

    @Override
    public void enter(ViewChangeEvent event) {
        
    }

}
