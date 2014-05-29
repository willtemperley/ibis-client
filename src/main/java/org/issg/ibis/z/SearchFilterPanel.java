package org.issg.ibis.z;

import java.util.List;

import javax.persistence.TypedQuery;

import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.QSpecies;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.view.QResourceDescription;
import org.issg.ibis.domain.view.ResourceDescription;
import org.jrc.persist.Dao;
import org.jrc.ui.SimplePanel;

import com.google.inject.Inject;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.SetPath;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

public class SearchFilterPanel extends TabSheet {

	private Dao dao;
	private QDSLFilterController<ResourceDescription> qdslFilterController;
	private FilterChangedListener l;

	public List<Species> getInvasiveSpp() {

		TypedQuery<Species> q = dao.getEntityManager().createNamedQuery(
				Species.INVASIVE, Species.class);
		return q.getResultList();
	}

	public List<Species> getThreatenedSpp() {

		TypedQuery<Species> q = dao.getEntityManager().createNamedQuery(
				Species.THREATENED, Species.class);
		return q.getResultList();
	}


	@Inject
	public SearchFilterPanel(final Dao dao) {

		this.dao = dao;

		qdslFilterController = new QDSLFilterController<ResourceDescription>(
				QResourceDescription.resourceDescription);
		
		addTab(getInvasivesPanel(), "Invasive Spp.");
		addTab(getThreatenedSpeciesPanel(), "Threatened Spp.");
		addTab(new Label(), "Location");

	}
	
	private VerticalLayout getInvasivesPanel() {
		VerticalLayout vl = new VerticalLayout();
		ComboBox cb = getSearchCombo(getInvasiveSpp(), QResourceDescription.resourceDescription.species);
		cb.setCaption("Invasive species");
		vl.addComponent(cb);

		return vl;
	}
	
	private VerticalLayout getThreatenedSpeciesPanel() {
		VerticalLayout vl = new VerticalLayout();
		ComboBox cb2 = getSearchCombo(getThreatenedSpp(), QResourceDescription.resourceDescription.species);
		cb2.setCaption("Threatened species");
		vl.addComponent(cb2);
		return vl;
	}
	

	private <X> ComboBox getSearchCombo(List<X> spp, SetPath<X, ? extends EntityPathBase<X>> setPath) {
		ComboBox cb = new ComboBox();
		for (X sp : spp) {
			cb.addItem(sp);
		}
		addSearchCombo(cb, setPath);
		return cb;
	}

	public <X> void addSearchCombo(ComboBox cb, SetPath<X, ? extends EntityPathBase<X>> setPath) {
		cb.setImmediate(true);

		qdslFilterController.addContainsField(cb, setPath);

		cb.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// List<ResourceDescription> x = qdslFilterController
				// .getResults(dao.getEntityManager());
				fireValueChanged();
			}

		});

		addComponent(cb);
	}

	
	public interface FilterChangedListener {
		public void onFilterChanged();
	}

	private void fireValueChanged() {
		if (l != null) {
			l.onFilterChanged();
		}
	}

	public void addValueChangeListener(FilterChangedListener l) {
		this.l = l;
	}
	
	public List<ResourceDescription> getResults() {
		List<ResourceDescription> x = qdslFilterController.getResults(dao.getEntityManager());
		return x;
	}
}
