package org.issg.ibis.qdsl.experimental;

import java.util.List;

import javax.persistence.TypedQuery;

import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.view.LocationView;
import org.issg.ibis.domain.view.LocationView_;
import org.issg.ibis.domain.view.QLocationView;
import org.issg.ibis.perspective.species.SpeciesSummaryController;
import org.jrc.persist.Dao;
import org.jrc.server.lec.LazyEntityContainer;
import org.vaadin.addons.form.field.filter.FilterPanel;

import com.google.inject.Inject;
import com.mysema.query.types.expr.BooleanExpression;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class LocationSearch extends HorizontalLayout implements View, QdslQueryListener {

	private SpeciesSummaryController view;
	private Dao dao;
	private  LazyEntityContainer<LocationView> lec;

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	@Inject
	public LocationSearch(Dao dao) {
		
		this.dao = dao;
		setSizeFull();
		lec = new LazyEntityContainer<LocationView>(QLocationView.locationView, LocationView.class, dao);
		setSpacing(true);
		
		/*
		 * 
		 */
		FilterController<LocationView> fc = new FilterController<LocationView>(QLocationView.locationView, dao);
		fc.addValueChangeListener(this);
		
		
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		
		Component f1 = fc.createFilterField(QLocationView.locationView.country);
		Component f2 = fc.createFilterField(QLocationView.locationView.name);
		
        vl.addComponent(f1);
        vl.addComponent(f2);
		

		SimpleTable<LocationView> table = getLocationViewTable(lec);
		
		vl.addComponent(table);
		vl.setExpandRatio(table, 1);
		addComponent(vl);
		
	}
	
	private SimpleTable<LocationView> getLocationViewTable(LazyEntityContainer<LocationView> lec) {

		SimpleTable<LocationView> table = new SimpleTable<LocationView>();
		table.setContainerDataSource(lec);

		table.setHeight("100%");
		table.setWidth("100%");

		table.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(Property.ValueChangeEvent event) {

				/*
				 * The 1:1 view-entity pattern works nicely.
				 */
				LocationView s = (LocationView) event.getProperty().getValue();
				Location loc = dao.find(Location.class, s.getId());

			}
		});
		
		table.addColumn(LocationView_.name);
		table.addColumn(LocationView_.country);
		table.build();

		return table;
	}

	@Override
	public void fireFilterChanged(List<BooleanExpression> expressions) {
		
		lec.setFilters(expressions);
		
	}
	

}
