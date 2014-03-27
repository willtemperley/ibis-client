package org.issg.ibis.perspective.species;

import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.Species_;
import org.issg.ibis.perspective.shared.SpeciesLinkColumn;
import org.jrc.persist.Dao;
import org.vaadin.maddon.ListContainer;

import it.jrc.form.editor.EntityTable;

import com.google.inject.Inject;
import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

public class SpeciesSearch extends VerticalLayout implements View {

	private ListContainer<Species> speciesImpactContainer = new ListContainer<Species>(Species.class);

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	@Inject
	public SpeciesSearch(Dao dao) {
		
		setSizeFull();
		
		addComponent(getSpeciesTable());
		
		speciesImpactContainer.addAll(dao.all(Species.class));
		
	}
	
	private EntityTable<Species> getSpeciesTable() {

		EntityTable<Species> table = new EntityTable<Species>(
				speciesImpactContainer);

		table.addGeneratedColumn("id", new SpeciesLinkColumn());
		table.addColumns(Species_.name, Species_.commonName, Species_.redlistCategory);

		table.setHeight("100%");
		table.setWidth("100%");
		table.setPageLength(100);

		table.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(Property.ValueChangeEvent event) {

				Species si = (Species) event.getProperty().getValue();
				System.out.println(si);
//				speciesImpactSelected(si);
			}
		});

		return table;
	}

}
