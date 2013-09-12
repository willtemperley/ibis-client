package org.issg.ibis.client.js;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vividsolutions.jts.geom.Envelope;

@JavaScript({"http://openlayers.org/api/OpenLayers.js", "https://maps.googleapis.com/maps/api/js?sensor=false", "app://VAADIN/js/openlayers_library.js", "app://VAADIN/js/openlayers-connector.js"})
public class OpenLayersMap extends AbstractJavaScriptComponent {
    public interface ValueChangeListener extends Serializable {
        void valueChange();
    }
    List<ValueChangeListener> listeners =
            new ArrayList<ValueChangeListener>();
    
    public OpenLayersMap() {
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

    @Override
    protected OpenLayersMapState getState() {
        return (OpenLayersMapState) super.getState();
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
}
