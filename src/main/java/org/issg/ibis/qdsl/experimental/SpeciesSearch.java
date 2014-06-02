package org.issg.ibis.qdsl.experimental;

import java.util.List;

import org.issg.ibis.ViewModule;
import org.issg.ibis.domain.QSpecies;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.issg.ibis.domain.Species_;
import org.issg.ibis.domain.view.QSpeciesImpactView;
import org.issg.ibis.domain.view.SpeciesImpactView;
import org.issg.ibis.domain.view.SpeciesImpactView_;
import org.issg.ibis.perspective.shared.SpeciesLinkColumn;
import org.issg.ibis.perspective.species.SpeciesSummaryController;
import org.jrc.persist.Dao;
import org.jrc.server.lec.LazyEntityContainer;
import org.jrc.ui.HtmlHeader;
import org.jrc.ui.SimplePanel;
import org.vaadin.maddon.ListContainer;

import it.jrc.form.editor.EntityTable;

import com.google.inject.Inject;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.StringPath;
import com.vaadin.data.Property;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class SpeciesSearch extends VerticalLayout implements View, QdslQueryListener {

	private SpeciesSummaryController view;

	private Dao dao;

	private LazyEntityContainer<SpeciesImpactView> lec;

	private SearchPanel vl = new SearchPanel();

	private FilterController<SpeciesImpactView> fc;


	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	@Inject
	public SpeciesSearch(Dao dao) {
		
		this.dao = dao;
		setSizeFull();
//		setSpacing(true);
		
		/*
		 * Do we use a view class for filters?
		 */
		vl.setSizeFull();
		vl.addStyleName("search-panel");
		vl.addComponent(new HtmlHeader("Search Species and Impacts"));
		lec = new LazyEntityContainer<SpeciesImpactView>(QSpeciesImpactView.speciesImpactView, SpeciesImpactView.class, dao);
		fc = new FilterController<SpeciesImpactView>(QSpeciesImpactView.speciesImpactView, dao);
		fc.addValueChangeListener(this);
		
		addSearchField(QSpeciesImpactView.speciesImpactView.country, "Filter by Country");
		addSearchField(QSpeciesImpactView.speciesImpactView.biologicalStatus, "Filter by Biological Status");
		addSearchField(QSpeciesImpactView.speciesImpactView.impactMechanism, "Filter by Impact Mechanism");
		addSearchField(QSpeciesImpactView.speciesImpactView.location, "Filter by Location");
		addSearchField(QSpeciesImpactView.speciesImpactView.invasiveSpecies, "Filter by Invasive Species");
		addSearchField(QSpeciesImpactView.speciesImpactView.nativeSpecies, "Filter by Native Species");

		SimpleTable<SpeciesImpactView> table = getSpeciesTable(lec);
		
		addComponent(vl);
		vl.setHeight("320px");
		addComponent(table);
		setExpandRatio(table, 1);
		
	}

	private void addSearchField(StringPath stringPath, String caption) {
		StringFieldInterface f = fc.createFilterField(stringPath, true);
		vl.addComponent(f);
		f.setWidth("300px");
		f.setCaption(caption);
	}
	
	private SimpleTable<SpeciesImpactView> getSpeciesTable(LazyEntityContainer<SpeciesImpactView> lec2) {

		SimpleTable<SpeciesImpactView> table = new SimpleTable<SpeciesImpactView>();
		table.setContainerDataSource(lec2);

		table.addColumn(SpeciesImpactView_.nativeSpecies);
		table.addColumn(SpeciesImpactView_.invasiveSpecies);
		table.addColumn(SpeciesImpactView_.impactMechanism);
		table.addColumn(SpeciesImpactView_.biologicalStatus);
		table.addColumn(SpeciesImpactView_.location);
		table.addColumn(SpeciesImpactView_.country);

		table.setHeight("100%");
		table.setWidth("100%");

		table.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(Property.ValueChangeEvent event) {
				
				//MADDON!! TYPESAFE TABLES

				SpeciesImpactView s = (SpeciesImpactView) event.getProperty().getValue();
				SpeciesImpact si = dao.find(SpeciesImpact.class, s.getId());
				
				//MADDON!! Navigate to.  In View??
				Navigator nav = UI.getCurrent().getNavigator();
				nav.navigateTo(ViewModule.SPECIES_PERSPECTIVE + "/" + si.getThreatenedSpecies().getId());

			}
		});

		table.build();
		return table;
	}

	@Override
	public void fireFilterChanged(List<BooleanExpression> expressions) {
		lec.setFilters(expressions);
	}
	

}
