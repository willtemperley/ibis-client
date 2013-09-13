package org.issg.ibis.client.js;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vividsolutions.jts.geom.Envelope;

@JavaScript({"http://cdn.leafletjs.com/leaflet-0.6.4/leaflet.js", "app://VAADIN/js/leaflet_library.js", "app://VAADIN/js/leafletmap-connector.js"})
public class LeafletMap extends AbstractJavaScriptComponent {
    public interface ValueChangeListener extends Serializable {
        void valueChange();
    }
    List<ValueChangeListener> listeners =
            new ArrayList<ValueChangeListener>();
    
    public LeafletMap() {
//        addFunction("onClick", new JavaScriptFunction() {
//            @Override
//            public void call(JSONArray arguments)
//                    throws JSONException {
//                getState().setValue(arguments.getString(0));
//                for (ValueChangeListener listener: listeners)
//                    listener.valueChange();
//            }
//        });
    }
    
    /*
     * Bounds
     */
    public void setBounds(String value) {
        getState().setBounds(value);
    }
    
    public String getBounds() {
        return getState().getBounds();
    }
    
    public void setSpecies(String species) {
        getState().setSpecies(species);
    }
    
    public String getSpecies() {
        return getState().getSpecies();
    }
    

    @Override
    protected LeafletMapState getState() {
        return (LeafletMapState) super.getState();
    }

    public void addValueChangeListener(ValueChangeListener valueChangeListener) {
        listeners.add(valueChangeListener);
    }

    public void zoomTo(Envelope env) {
        
        if (env == null) {
            return;
        }
        
        JSONArray master = new JSONArray();

        master.put(new double[] {env.getMinY(), env.getMinX()});
        master.put(new double[] {env.getMaxY(), env.getMaxX()});
        
        String json = master.toString();
        
        setBounds(json);
        
    }
    public void zoomTo(Envelope env, String species) {
        
        if (env == null) {
            return;
        }
        
        JSONArray master = new JSONArray();

        master.put(new double[] {env.getMinY(), env.getMinX()});
        master.put(new double[] {env.getMaxY(), env.getMaxX()});
        
        String json = master.toString();
        
        setBounds(json);
        setSpecies(species);
        
    }
}
