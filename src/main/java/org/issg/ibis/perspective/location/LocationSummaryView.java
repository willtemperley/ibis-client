package org.issg.ibis.perspective.location;

import org.issg.ibis.client.content.SimpleContentController;
import org.issg.ibis.domain.Location;
import org.jrc.ui.SimplePanel;

public class LocationSummaryView extends SimpleContentController {

    public LocationSummaryView(SimplePanel panel) {
        super(panel);
    }

    public void setLocation(Location location) {

        setContent(location.getLocationSummaries());
        
    }

}
