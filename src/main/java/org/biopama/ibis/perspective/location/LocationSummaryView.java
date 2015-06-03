package org.biopama.ibis.perspective.location;

import org.biopama.ibis.perspective.shared.SimpleContentController;
import org.issg.ibis.domain.Location;

import com.vaadin.ui.CssLayout;

public class LocationSummaryView extends SimpleContentController {

    public LocationSummaryView(CssLayout panel) {
        super(panel);
    }

    public void setLocation(Location location) {

        setContent(location.getLocationSummaries());
        
    }

}
