package org.issg.ibis.client.js;

import com.vaadin.shared.ui.JavaScriptComponentState;


public class LeafletMapState extends JavaScriptComponentState {
    
    private String bounds;
    private String species;
    private String locations;
    
    public String getBounds() {
        return bounds;
    }

    public void setBounds(String bounds) {
        this.bounds = bounds;
    }
    
    public String getSpecies() {
        return this.species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }
    
    public String getLocations() {
        return this.locations;
    }

    public void setLocations(String locationJson) {
        this.locations = locationJson;
    }


}