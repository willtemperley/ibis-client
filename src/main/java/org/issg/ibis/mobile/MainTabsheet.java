package org.issg.ibis.mobile;

import com.vaadin.addon.touchkit.ui.TabBarView;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet.Tab;

/**
 * This is the main view for Parking application. It displays a tabbar via one
 * can choose one of the sub views.
 */
public class MainTabsheet extends TabBarView {

    public MainTabsheet() {
        /*
         * Populate main views
         */

        LocationView component = new LocationView();

		addTab(component, "maptab", "24h Map");

        addTab(new SpeciesView(), "shiftstab", "Shifts");


        setSelectedTab(component);
    }

    private void addTab(final Component component, final String styleName,
            final String caption) {
        Tab tab = addTab(component);
        tab.setStyleName(styleName);
        tab.setCaption(caption);
    }

}

