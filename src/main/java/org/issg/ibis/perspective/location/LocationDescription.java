package org.issg.ibis.perspective.location;

import org.issg.ibis.domain.Location;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;

public class LocationDescription extends FormLayout {

	private Label locationType = new Label();
	private Label iucnCategory = new Label();
	private Label country = new Label();


	public LocationDescription() {
		addComponent(locationType);
		addComponent(iucnCategory);
		addComponent(country);

         locationType.setCaption("Location type:");
         iucnCategory.setCaption("IUCN category:");
         country.setCaption("Country:");
	}

	public void setLocation(Location location) {
		locationType.setValue(location.getLocationType().toString());
		country.setValue(location.getCountry().getName());

		if (location.getIucnCat() != null) {
			iucnCategory.setValue(location.getIucnCat());
			iucnCategory.setVisible(true);
		} else {
			iucnCategory.setVisible(false);
		}
	}
}
