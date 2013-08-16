package org.issg.ibis.client.js;

import com.vaadin.shared.ui.JavaScriptComponentState;


public class LeafletMapState extends JavaScriptComponentState {
    
    private String bounds;
    
    public String getBounds() {
        return bounds;
    }

    public void setBounds(String bounds) {
        this.bounds = bounds;
    }

}