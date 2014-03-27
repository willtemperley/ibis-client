package org.issg.ibis.perspective.location;

import org.issg.ibis.client.content.SimpleContentPanel;
import org.issg.ibis.domain.Location;
import org.jrc.ui.SimplePanel;

public class LocationSummaryView extends SimpleContentPanel {

    public LocationSummaryView(SimplePanel panel) {
        super(panel);
    }

    public void setLocation(Location location) {

        addContent(location.getLocationSummaries());
        
    }

}
