package org.issg.ibis;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CssLayout;

public class HeaderView extends CssLayout implements View {
    
    
    @Inject
    public HeaderView(AccountOptionView menuView) {
        this.setSizeFull();
        this.addComponent(menuView);
        this.setStyleName("header");
    }
    

    @Override
    public void enter(ViewChangeEvent event) {
        
    }

}
