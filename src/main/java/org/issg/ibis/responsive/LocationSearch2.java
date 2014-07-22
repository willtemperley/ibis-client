package org.issg.ibis.responsive;

import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.issg.ibis.ViewModule;
import org.issg.ibis.domain.Country;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.QCountry;
import org.issg.ibis.domain.QLocation;
import org.issg.ibis.domain.QSpeciesImpact;
import org.issg.ibis.domain.Region;
import org.jrc.edit.Dao;
import org.jrc.ui.HtmlLabel;
import org.vaadin.addons.lec.EntityTable;
import org.vaadin.maddon.ListContainer;
import org.vaadin.maddon.fields.MValueChangeEvent;
import org.vaadin.maddon.fields.MValueChangeListener;
import org.vaadin.maddon.fields.TypedSelect;

import com.google.inject.Inject;
import com.mysema.query.jpa.impl.JPAQuery;
import com.vaadin.data.Property;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field.ValueChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class LocationSearch2 extends HorizontalLayout {

	private Dao dao;
	private TypedSelect<Country> countrySelector;
	private EntityTable<Location> locationSelector;
	private LocationCaption locationCaption;
	private LocationCaption countryCaption;

	private ListContainer<Location> locationContainer = new ListContainer<Location>(Location.class);

	@Inject
	public LocationSearch2(Dao dao) {
		this.dao = dao;
		setSpacing(true);
		setSizeFull();

		
		VerticalLayout leftPanel = new VerticalLayout();
		leftPanel.setSpacing(true);
		leftPanel.addComponent(new HtmlLabel("Select a location to view. Locations can be filtered by country and region."));
		addComponent(leftPanel);
		
		TypedSelect<Region> regionSelector = getRegionSelector();
		leftPanel.addComponent(regionSelector);
		regionSelector.setWidth("200px");

		countrySelector = getCountrySelector();
		countrySelector.setWidth("200px");
		leftPanel.addComponent(countrySelector);

		locationSelector = getLocationTable();
		locationSelector.setSizeFull();
//		locationSelector.setHeight("300px");
		addComponent(locationSelector);
		
		locationCaption = LocationCaption.getInstance(getLocale(), LocationCaption.LOCATION_BUNDLE);
		countryCaption = LocationCaption.getInstance(getLocale(), LocationCaption.COUNTRY_BUNDLE);

	}

	private EntityTable<Location> getLocationTable() {
		
		
		EntityTable<Location> select = new EntityTable<Location>(locationContainer);
		select.setSizeFull();


		TypedQuery<Location> q = dao.get().createNamedQuery(
				Location.HAS_IMPACT, Location.class);
		List<Location> resultList = q.getResultList();
		locationContainer.addAll(resultList);
		select.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				// TODO Auto-generated method stub
				Location val = (Location) event.getProperty().getValue();
				if (val != null) {
					Navigator nav = UI.getCurrent().getNavigator();
					nav.navigateTo(ViewModule.LOCATION_PERSPECTIVE + "/"
							+ val.getId());
				}
			}
		});
		
		select.addColumns("name", "locationType");
		return select;
	}

	private TypedSelect<Region> getRegionSelector() {
		TypedSelect<Region> select = new TypedSelect<Region>("Region").withSelectType(ComboBox.class);
	
		JPAQuery q = new JPAQuery(dao.get());
		QSpeciesImpact si = QSpeciesImpact.speciesImpact;
		QCountry c = QCountry.country;
		q = q.from(c, si).where(c.eq(si.location.country));
		q = q.distinct();
		List<Region> results = q.list(c.region);
		select.setOptions(results);
	
		select.addTypedValueChangeListener(new MValueChangeListener<Region>() {
	
			@Override
			public void valueChange(MValueChangeEvent<Region> event) {
				
				Region region = event.getValue();
	
				List<Country> countries = getCountriesForRegion(region);
				countrySelector.setOptions(countries);
				
				List<Location> locations = getLocationsForRegion(region);
				locationContainer.setCollection(locations);
						
				
				String regionName = null;
				if(region != null) {
					regionName = region.getName();
				}
				String countryCaption2 = getCountryCaption(regionName, countries);
				countrySelector.setCaption(countryCaption2);
				String locationCaption2 = getLocationCaption(regionName, locations);
				locationSelector.setCaption(locationCaption2);
				
			}
		});
	
		return select;
	}
	protected String getCountryCaption(String name, List<?> l) {
		return countryCaption.getMessage(name, l.size());
	}
	protected String getLocationCaption(String name, List<?> l) {
		return locationCaption.getMessage(name, l.size());
	}

	private TypedSelect<Country> getCountrySelector() {
		TypedSelect<Country> select = new TypedSelect<Country>("Country").withSelectType(ComboBox.class);

		List<Country> results = getCountriesForRegion(null);
		select.setOptions(results);

		select.addTypedValueChangeListener(new MValueChangeListener<Country>() {

			@Override
			public void valueChange(MValueChangeEvent<Country> event) {
				Country c = event.getValue();
				List<Location> locations = getLocationsForCountry(c);
				locationContainer.setCollection(locations);
				if (c != null) {
					String caption = getLocationCaption(c.getName(), locations);
					locationSelector.setCaption(caption);
				} else {
					//Locations for region?
				}
			}
		});

		return select;
	}
	
	private List<Country> getCountriesForRegion(Region r) {
		JPAQuery q = new JPAQuery(dao.get());

		QSpeciesImpact si = QSpeciesImpact.speciesImpact;
		QCountry c = QCountry.country;

		q.from(si, c).where(si.location.country.eq(c));

		if (r != null) {
			q.where(c.region.eq(r));
		}
		q = q.distinct();
		return q.list(c);

	}
	
	private List<Location> getLocationsForCountry(Country c) {
		JPAQuery q = new JPAQuery(dao.get());
		QSpeciesImpact si = QSpeciesImpact.speciesImpact;
		QLocation l = QLocation.location;
		q = q.from(l, si).where(si.location.eq(l));
		if (c != null) {
			q.where(l.country.eq(c));
		}
		q.distinct();
		return q.list(l);
	}

	private List<Location> getLocationsForRegion(Region r) {
		JPAQuery q = new JPAQuery(dao.get());
		QSpeciesImpact si = QSpeciesImpact.speciesImpact;
		QLocation l = QLocation.location;

		q = q.from(l, si).where(si.location.eq(l));

		if (r != null) {
			q.where(QLocation.location.country.region.eq(r));
		}
		q.distinct();
		return q.list(QLocation.location);
	}
}
