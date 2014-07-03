package org.issg.ibis.responsive;

import it.jrc.form.editor.EditorPanelHeading;

import java.util.List;

import javax.persistence.TypedQuery;

import org.issg.ibis.ViewModule;
import org.issg.ibis.domain.Species;
import org.issg.ibis.qdsl.search.TypedSelect;
import org.jrc.persist.Dao;
import org.jrc.ui.SimplePanel;

import com.google.inject.Inject;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class SpeciesSearch extends VerticalLayout {

	private Dao dao;
	

	@Inject
	public SpeciesSearch(Dao dao) {
		this.dao = dao;
//		addComponent(new EditorPanelHeading("Search by Species"));
		addComponent(getSpeciesSelector(Species.NATIVE, "Select Native Species"));
		addComponent(getSpeciesSelector(Species.INVASIVE, "Select Invasive Alien Species"));
	}

	private Component getSpeciesSelector(String speciesType, String caption) {
		TypedSelect<Species> speciesSelector = new TypedSelect<Species>(
				caption);
		speciesSelector.addStyleName("species-selector");
		speciesSelector.setWidth("200px");
		speciesSelector.addVCL(new TypedSelect.ValueChangeListener<Species>() {

			@Override
			public void onValueChange(Species val) {
				if (val != null) {
					// TODO Auto-generated method stub
					Navigator nav = UI.getCurrent().getNavigator();
					nav.navigateTo(ViewModule.SPECIES_PERSPECTIVE + "/"
							+ val.getId());
				}

			}
		});
		TypedQuery<Species> q = dao.getEntityManager().createNamedQuery(
				speciesType, Species.class);
		List<Species> resultList = q.getResultList();
		for (Species species : resultList) {
			speciesSelector.addItem(species);
		}
		return speciesSelector;
	}

}
