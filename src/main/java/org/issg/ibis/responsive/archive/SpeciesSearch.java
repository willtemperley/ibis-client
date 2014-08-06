package org.issg.ibis.responsive.archive;

import java.util.List;

import javax.persistence.TypedQuery;

import org.issg.ibis.ViewModule;
import org.issg.ibis.domain.Species;
import org.jrc.edit.Dao;
import org.vaadin.maddon.fields.MValueChangeEvent;
import org.vaadin.maddon.fields.MValueChangeListener;
import org.vaadin.maddon.fields.TypedSelect;

import com.google.inject.Inject;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class SpeciesSearch extends VerticalLayout {

	private Dao dao;
	

	public SpeciesSearch(Dao dao) {
		this.dao = dao;
//		addComponent(new EditorPanelHeading("Search by Species"));
		addComponent(getSpeciesSelector(Species.NATIVE, "Select Native Species"));
		addComponent(getSpeciesSelector(Species.INVASIVE, "Select Invasive Alien Species"));
	}

	private Component getSpeciesSelector(String speciesType, String caption) {
		TypedSelect<Species> speciesSelector = new TypedSelect<Species>(
				caption).withSelectType(ComboBox.class);
		speciesSelector.addStyleName("species-selector");
		speciesSelector.setWidth("200px");
		speciesSelector.addMValueChangeListener(new MValueChangeListener<Species>() {


			@Override
			public void valueChange(MValueChangeEvent<Species> event) {
				Species val = event.getValue();
				if (val != null) {
					// TODO Auto-generated method stub
					Navigator nav = UI.getCurrent().getNavigator();
					nav.navigateTo(ViewModule.SPECIES_PERSPECTIVE + "/"
							+ val.getId());
				}
				
			}
		});
		TypedQuery<Species> q = dao.get().createNamedQuery(
				speciesType, Species.class);
		List<Species> resultList = q.getResultList();
		speciesSelector.setOptions(resultList);
		return speciesSelector;
	}

}
