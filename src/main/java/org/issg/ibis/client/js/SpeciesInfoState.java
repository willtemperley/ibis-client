package org.issg.ibis.client.js;

import com.vaadin.shared.ui.JavaScriptComponentState;


public class SpeciesInfoState extends JavaScriptComponentState {
    
    private String species;
    
    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

}