package org.issg.ibis.client.js;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.issg.ibis.domain.Species;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;

@JavaScript({"http://cdn.leafletjs.com/leaflet-0.6.4/leaflet.js", "app://VAADIN/js/species_library.js", "app://VAADIN/js/speciesinfo-connector.js"})
public class SpeciesInfo extends AbstractJavaScriptComponent {
    public interface ValueChangeListener extends Serializable {
        void valueChange();
    }
    List<ValueChangeListener> listeners =
            new ArrayList<ValueChangeListener>();
    
    public SpeciesInfo() {
        addStyleName("species-info");
        addFunction("onClick", new JavaScriptFunction() {
            @Override
            public void call(JSONArray arguments)
                    throws JSONException {
                getState().setSpecies(arguments.getString(0));
                for (ValueChangeListener listener: listeners)
                    listener.valueChange();
            }
        });
    }
    
    public void setSpecies(Species species, String imgUrl) {
        
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", species.getName());
            obj.put("commonName", species.getCommonName());
            obj.put("imgUrl", imgUrl);
            
            String redlistImgUrl = String.format("/ibis-client/VAADIN/themes/dashboard/redlist/240px-Status_iucn3.1_%s.svg.png", species.getRedlistCategory().getRedlistCode());
            obj.put("redlistImgUrl", redlistImgUrl);
            
            setSpecies(obj.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public void setSpecies(String value) {
        getState().setSpecies(value);
    }
    
    public String getSpecies() {
        return getState().getSpecies();
    }

    @Override
    protected SpeciesInfoState getState() {
        return (SpeciesInfoState) super.getState();
    }

    public void addValueChangeListener(ValueChangeListener valueChangeListener) {
        listeners.add(valueChangeListener);
    }
    

}
