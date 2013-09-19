package org.issg.ibis.client.js;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vividsolutions.jts.geom.Envelope;

@JavaScript({ "http://cdn.leafletjs.com/leaflet-0.6.4/leaflet.js",
        "app://VAADIN/js/leaflet_library.js",
        "app://VAADIN/js/leafletmap-connector.js" })
public class LeafletMap extends AbstractJavaScriptComponent {
    public interface ValueChangeListener extends Serializable {
        void valueChange();
    }

    List<ValueChangeListener> listeners = new ArrayList<ValueChangeListener>();

    JSONArray master = new JSONArray();

    public LeafletMap() {
    }

    /*
     * Bounds
     */
    public String getBounds() {
        return getState().getBounds();
    }

    public void setBounds(String value) {
        getState().setBounds(value);
    }

    /*
     * Locations
     */
    public String getLocations() {
        return getState().getLocations();
    }

    public void setLocations(String locationJson) {
        getState().setLocations(locationJson);
    }

    /*
     * Species
     */
    public String getSpecies() {
        return getState().getSpecies();
    }

    public void setSpecies(String species) {
        getState().setSpecies(species);
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

        master.put(new double[] { env.getMinY(), env.getMinX() });
        master.put(new double[] { env.getMaxY(), env.getMaxX() });

        String json = master.toString();

        setBounds(json);

    }

    public void addPolygons(List<String> res) {

        for (String string : res) {
            try {
                JSONObject jso;
                jso = new JSONObject(string);
                master.put(jso);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String json = master.toString();
        getState().setLocations(json);
    }
}
