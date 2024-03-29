package org.biopama.search;

import java.util.List;

import javax.persistence.TypedQuery;

import org.biopama.edit.Dao;
import org.biopama.ibis.ViewModule;
import org.biopama.ui.HtmlLabel;
import org.issg.ibis.domain.Species;
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

	@Inject
	public SpeciesSearch(Dao dao, String sppType) {
		this.dao = dao;
		setSpacing(true);
		if (sppType.equals(Species.INVASIVE)) {
			addComponent(getSpeciesSelector(sppType, "Select Invasive Alien Species"));
			addComponent(new HtmlLabel("View the distribution and the nature of impacts caused by these species"));
		} else {
			addComponent(getSpeciesSelector(sppType, "Select Native Species"));
			addComponent(new HtmlLabel("View the species distribution and how this is impacted by invasive alien species"));
		}
	}

	private Component getSpeciesSelector(String speciesType, String caption) {
		TypedSelect<Species> speciesSelector = new TypedSelect<Species>(
				caption).withSelectType(ComboBox.class);
		speciesSelector.setWidth(300, Unit.PIXELS);
		speciesSelector.addStyleName("species-select");
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
