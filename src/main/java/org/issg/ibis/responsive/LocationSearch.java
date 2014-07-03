package org.issg.ibis.responsive;

import it.jrc.form.editor.EditorPanelHeading;

import java.util.List;

import javax.persistence.TypedQuery;

import org.issg.ibis.ViewModule;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.QLocation;
import org.issg.ibis.domain.QSpeciesImpact;
import org.issg.ibis.qdsl.search.TypedSelect;
import org.issg.ibis.qdsl.search.TypedSelect.ValueChangeListener;
import org.jrc.persist.Dao;
import org.jrc.persist.adminunits.Country;
import org.jrc.persist.adminunits.QCountry;
import org.jrc.ui.SimplePanel;

import com.google.inject.Inject;
import com.mysema.query.jpa.impl.JPAQuery;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class LocationSearch extends VerticalLayout {

	private Dao dao;
	private TypedSelect<Country> countrySelector;
	private TypedSelect<Location> locationSelector;

	@Inject
	public LocationSearch(Dao dao) {
		this.dao = dao;

//		addComponent(new EditorPanelHeading("Search by Location"));
		

		countrySelector = getCountrySelector();
		countrySelector.setWidth("200px");
		addComponent(countrySelector);

		locationSelector = getLocationSelector();
		locationSelector.setWidth("200px");
		addComponent(locationSelector);


	}

	private TypedSelect<Location> getLocationSelector() {
		TypedSelect<Location> select = new TypedSelect<Location>("Location");
		TypedQuery<Location> q = dao.getEntityManager().createNamedQuery(
				Location.HAS_IMPACT, Location.class);
		for (Location l : q.getResultList()) {
			select.addItem(l);
		}
		select.addVCL(new TypedSelect.ValueChangeListener<Location>() {

			@Override
			public void onValueChange(Location val) {
				if (val != null) {
					Navigator nav = UI.getCurrent().getNavigator();
					nav.navigateTo(ViewModule.LOCATION_PERSPECTIVE + "/"
							+ val.getId());
				}

			}
		});
		return select;
	}

	private TypedSelect<Country> getCountrySelector() {
		TypedSelect<Country> select = new TypedSelect<Country>("Country");

		JPAQuery q = new JPAQuery(dao.getEntityManager());
		QSpeciesImpact si = QSpeciesImpact.speciesImpact;
		QCountry c = QCountry.country;
		q = q.from(c, si).where(c.eq(si.location.country));
		q = q.distinct();
		List<Country> results = q.list(c);
		for (Country country : results) {
			select.addItem(country);
		}

		select.addVCL(new TypedSelect.ValueChangeListener<Country>() {
			@Override
			public void onValueChange(Country val) {
				List<Location> x = getLocationsForCountry(val);
				locationSelector.removeAllItems();
				locationSelector.addItems(x);
			}
		});

		return select;
	}

	private List<Location> getLocationsForCountry(Country c) {
		JPAQuery q = new JPAQuery(dao.getEntityManager());
		q = q.from(QLocation.location);
		if (c != null) {
			q.where(QLocation.location.country.eq(c));
		}
		return q.list(QLocation.location);
	}
}
