package org.issg.ibis.qdsl.experimental;

import java.util.List;

import org.biopama.edit.Dao;
import org.issg.ibis.perspective.species.SpeciesSummaryController;

import com.google.inject.Inject;
import com.mysema.query.types.expr.BooleanExpression;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

public class SpeciesSearch extends VerticalLayout implements View, QdslQueryListener {

	private SpeciesSummaryController view;

	private Dao dao;

//	private LazyEntityContainer<NativeSpeciesImpactView> lec;
//
//	private SearchPanel vl = new SearchPanel();
//
//	private FilterController<NativeSpeciesImpactView> fc;


	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	@Inject
	public SpeciesSearch(Dao dao) {
		
//		this.dao = dao;
//		setSizeFull();
////		setSpacing(true);
//		
//		/*
//		 * Do we use a view class for filters?
//		 */
//		vl.setSizeFull();
//		vl.addStyleName("search-panel");
//		vl.addComponent(new HtmlHeader("Search Species and Impacts"));
//		lec = new LazyEntityContainer<NativeSpeciesImpactView>(QSpeciesImpactView.speciesImpactView, NativeSpeciesImpactView.class, dao);
//		fc = new FilterController<NativeSpeciesImpactView>(QSpeciesImpactView.speciesImpactView, dao);
//		fc.addValueChangeListener(this);
//		
//		addSearchField(QSpeciesImpactView.speciesImpactView.country, "Filter by Country");
//		addSearchField(QSpeciesImpactView.speciesImpactView.biologicalStatus, "Filter by Biological Status");
//		addSearchField(QSpeciesImpactView.speciesImpactView.impactMechanism, "Filter by Impact Mechanism");
//		addSearchField(QSpeciesImpactView.speciesImpactView.location, "Filter by Location");
//		addSearchField(QSpeciesImpactView.speciesImpactView.invasiveSpecies, "Filter by Invasive Species");
////		addSearchField(QSpeciesImpactView.speciesImpactView.nativeSpecies, "Filter by Native Species");
//
//		SimpleTable<NativeSpeciesImpactView> table = getSpeciesTable(lec);
//		
//		addComponent(vl);
//		vl.setHeight("320px");
//		addComponent(table);
//		setExpandRatio(table, 1);
//		
	}

	@Override
	public void fireFilterChanged(List<BooleanExpression> expressions) {
		// TODO Auto-generated method stub
		
	}

//	private void addSearchField(StringPath stringPath, String caption) {
//		StringFieldInterface f = fc.createFilterField(stringPath, true);
//		vl.addComponent(f);
//		f.setWidth("300px");
//		f.setCaption(caption);
//	}
	
//	private SimpleTable<NativeSpeciesImpactView> getSpeciesTable(LazyEntityContainer<NativeSpeciesImpactView> lec2) {
//
//		SimpleTable<NativeSpeciesImpactView> table = new SimpleTable<NativeSpeciesImpactView>();
//		table.setContainerDataSource(lec2);
//
//		table.addColumn(NativeSpeciesImpactView_.nativeSpecies);
//		table.addColumn(NativeSpeciesImpactView_.invasiveSpecies);
//		table.addColumn(NativeSpeciesImpactView_.impactMechanism);
//		table.addColumn(NativeSpeciesImpactView_.biologicalStatus);
//		table.addColumn(NativeSpeciesImpactView_.location);
//		table.addColumn(NativeSpeciesImpactView_.country);
//
//		table.setHeight("100%");
//		table.setWidth("100%");
//
//		table.addValueChangeListener(new Property.ValueChangeListener() {
//			public void valueChange(Property.ValueChangeEvent event) {
//				
//				//MADDON!! TYPESAFE TABLES
//
//				NativeSpeciesImpactView s = (NativeSpeciesImpactView) event.getProperty().getValue();
//				SpeciesImpact si = dao.find(SpeciesImpact.class, s.getId());
//				
//				//MADDON!! Navigate to.  In View??
//				Navigator nav = UI.getCurrent().getNavigator();
//				nav.navigateTo(ViewModule.SPECIES_PERSPECTIVE + "/" + si.getNativeSpecies().getId());
//
//			}
//		});
//
//		table.build();
//		return table;
//	}
//
//	@Override
//	public void fireFilterChanged(List<BooleanExpression> expressions) {
//		lec.setFilters(expressions);
//	}
//	

}
