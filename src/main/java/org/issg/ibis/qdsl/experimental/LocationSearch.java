package org.issg.ibis.qdsl.experimental;

import java.util.List;

import org.issg.ibis.ViewModule;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.view.LocationView;
import org.issg.ibis.domain.view.LocationView_;
import org.issg.ibis.domain.view.QLocationView;
import org.issg.ibis.perspective.species.SpeciesSummaryController;
import org.jrc.edit.Dao;
import org.jrc.ui.HtmlHeader;
import org.jrc.ui.SimplePanel;
import org.vaadin.addons.lec.LazyEntityContainer;

import com.google.inject.Inject;
import com.mysema.query.types.expr.BooleanExpression;
import com.vaadin.data.Property;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class LocationSearch extends VerticalLayout implements View, QdslQueryListener {

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
//		setSpacing(true);
		
		/*
		 * 
		 */
		final FilterController<LocationView> fc = new FilterController<LocationView>(QLocationView.locationView, dao);
		fc.addValueChangeListener(this);
		
		SearchPanel vl = new SearchPanel();
		vl.addComponent(new HtmlHeader("Search Locations"));
		vl.setHeight("250px");
		
		StringFieldInterface f1 = fc.createFilterField(QLocationView.locationView.country, true);
		f1.setWidth("300px");
		f1.setCaption("Filter by Country");
//		StringFieldInterface f2 = fc.createFilterField(QLocationView.locationView.name, false);
//		f2.setCaption("Filter by Name");
//		f2.setWidth("300px");

		StringFieldInterface f3 = fc.createFilterField(QLocationView.locationView.region, true);
		f3.setCaption("Filter by region");
		f3.setWidth("300px");
		
        vl.addComponent(f1);
//        vl.addComponent(f2);
        vl.addComponent(f3);

        Label label = new Label();
        vl.addComponent(label);
		Button c = new Button("Clear filters");
		vl.addComponent(c);
		c.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				fc.clear();
			}
		});
        
        /*
         * Adding to root 
         */
		addComponent(vl);
		SimpleTable<LocationView> table = getLocationViewTable(lec);
		addComponent(table);
		setExpandRatio(table, 1);
		
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
				if (s == null) {
					return;
				}
				Navigator nav = UI.getCurrent().getNavigator();
				nav.navigateTo(ViewModule.LOCATION_PERSPECTIVE + "/" + s.getId());

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
