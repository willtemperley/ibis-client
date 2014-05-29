package org.issg.ibis.qdsl.experimental;

import org.issg.ibis.domain.QSpecies;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.Species_;
import org.issg.ibis.perspective.shared.SpeciesLinkColumn;
import org.issg.ibis.perspective.species.SpeciesSummaryController;
import org.jrc.persist.Dao;
import org.jrc.server.lec.LazyEntityContainer;
import org.jrc.ui.SimplePanel;
import org.vaadin.maddon.ListContainer;

import it.jrc.form.editor.EntityTable;

import com.google.inject.Inject;
import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;

public class SpeciesSearch extends HorizontalLayout implements View {

	private SpeciesSummaryController view;
	private Dao dao;

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
		

		
		LazyEntityContainer<Species> lec = 
				new LazyEntityContainer<Species>(QSpecies.species1, Species.class, dao);

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
				s = dao.find(Species.class, s.getId());
				view.setSpecies(s);

			}
		});

		table.build();
		return table;
	}
	

}
