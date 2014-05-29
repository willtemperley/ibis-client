package org.issg.ibis.qdsl.experimental;

import java.util.List;

import org.issg.ibis.ViewModule;
import org.issg.ibis.domain.QSpecies;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.Species_;
import org.issg.ibis.domain.view.QSpeciesImpactView;
import org.issg.ibis.domain.view.SpeciesImpactView;
import org.issg.ibis.perspective.shared.SpeciesLinkColumn;
import org.issg.ibis.perspective.species.SpeciesSummaryController;
import org.jrc.persist.Dao;
import org.jrc.server.lec.LazyEntityContainer;
import org.jrc.ui.SimplePanel;
import org.vaadin.maddon.ListContainer;

import it.jrc.form.editor.EntityTable;

import com.google.inject.Inject;
import com.mysema.query.types.expr.BooleanExpression;
import com.vaadin.data.Property;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class SpeciesSearch extends HorizontalLayout implements View, QdslQueryListener {

	private SpeciesSummaryController view;
	private Dao dao;
	private LazyEntityContainer<Species> lec;

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	@Inject
	public SpeciesSearch(Dao dao) {
		
		this.dao = dao;
		setSizeFull();
		setSpacing(true);
		
		/*
		 * 
		 */
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		
		lec = new LazyEntityContainer<Species>(QSpecies.species1, Species.class, dao);
		
		/*
		 * 
		 */
//		final FilterController<SpeciesImpactView> fc = new FilterController<SpeciesImpactView>(QSpeciesImpactView.speciesImpactView, dao);
//		fc.addValueChangeListener(this);
//		
//		StringFieldInterface f1 = fc.createFilterField(QSpeciesImpactView.speciesImpactView.country, true);
//		vl.addComponent(f1);

		SimpleTable<Species> table = getSpeciesTable(lec);
		
		vl.addComponent(table);
		vl.setExpandRatio(table, 1);
		addComponent(vl);
		
	}
	
	private SimpleTable<Species> getSpeciesTable(LazyEntityContainer<Species> lec) {

		SimpleTable<Species> table = new SimpleTable<Species>();
		table.setContainerDataSource(lec);

		table.addGeneratedColumn(Species_.link, new SpeciesLinkColumn());
		table.addColumn(Species_.redlistCategory);

		table.setHeight("100%");
		table.setWidth("100%");

		table.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(Property.ValueChangeEvent event) {

				Species s = (Species) event.getProperty().getValue();
				Navigator nav = UI.getCurrent().getNavigator();
				nav.navigateTo(ViewModule.SPECIES_PERSPECTIVE + "/" + s.getId());

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
