package org.issg.ibis.z;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.issg.ibis.client.deprecated.IbisMap;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.view.QResourceDescription;
import org.issg.ibis.domain.view.ResourceDescription;
import org.issg.ibis.perspective.shared.LayerViewer;
import org.issg.ibis.z.SearchFilterPanel.FilterChangedListener;
import org.jrc.persist.Dao;
import org.jrc.ui.SimplePanel;
import org.jrc.ui.baseview.TwinPanelView;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.markercluster.LMarkerClusterGroup;
import org.vaadin.addons.guice.uiscope.UIScoped;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;
import com.vividsolutions.jts.geom.Point;

@UIScoped
public class SearchPerspective extends TwinPanelView implements View {

	private Dao dao;
	private LayerViewer layerViewer;

	// Still may be a valid approach
	// IbisMap ibisMap;

	private List<Location> getLocations() {
		TypedQuery<Location> q = dao.getEntityManager().createQuery(
				"from Location", Location.class);
		return q.getResultList();
	}
	
	private <X> void addItems(List<X> items, ComboBox cb) {
		for (X x : items) {
			cb.addItem(x);
		}
	}

	@Inject
	public SearchPerspective(final Dao dao) {

		setSizeFull();

		this.dao = dao;
        this.layerViewer = new LayerViewer();

		{
			final SearchTable t = new SearchTable(dao);

			final SearchFilterPanel searchPanel = new SearchFilterPanel(dao);

//			ComboBox cb = new ComboBox();
//			addItems(locations, cb);
			List<Location> locations = getLocations();
			layerViewer.addLocations(locations);
//
//			searchPanel.addSearchCombo(cb, QResourceDescription.resourceDescription.locations);
//			cb.setCaption("Location");
//			
			searchPanel.addValueChangeListener(new FilterChangedListener() {

				@Override
				public void onFilterChanged() {
					List<ResourceDescription> results = searchPanel.getResults();

					List<Location> locs = new ArrayList<Location>();
					for (ResourceDescription rd : results) {
						String x = rd.getId();
						String[] arr = x.split("/");
						if (arr[0].equals("Location")) {
							String strId = arr[1];
							Long id = Long.valueOf(strId);
							Location loc = dao.find(Location.class, id);
							locs.add(loc);
						}
					}

					layerViewer.setSearchResults(locs);
					t.setResults(results);
				}
			});

			// one-off
			t.setResults(searchPanel.getResults());

			searchPanel.setHeight("300px");

			VerticalLayout vl = new VerticalLayout();

			getLeftPanel().addComponent(vl);
			

			/*
			 * L/R panel alignment
			 */
			getLeftPanel().setWidth("500px");
			setExpandRatio(getRightPanel(), 1);;


			/*
			 * Perform the layout
			 */
			setExpandRatio(getRightPanel(), 1);
			vl.setSizeFull();
			vl.addComponent(searchPanel);
			vl.addComponent(t);
			vl.setExpandRatio(t, 1);
		}

		{
			getRightPanel().addComponent(layerViewer);
			layerViewer.setSizeFull();
		}

		// searchPanel.addSearchListener(new SearchSelectEventListener() {
		// @Override
		// public void onSelect(FacetedSearch facetedSearch) {
		//
		// String name = facetedSearch.getResourceType().getName();
		// System.out.println(name);
		// //FIXME so fragile!!
		// String s = facetedSearch.getId();
		//
		// String[] arr = s.split("/");
		// String o = arr[1];
		// Long i = Long.valueOf(o);
		//
		// if (name.equals("Species")) {
		// Species sp = dao.find(Species.class, i);
		//
		// // speciesSummaryView.setSpecies(sp);
		// // tabSheet.setSelectedTab(1);
		//
		// ibisMap.setSpecies(sp);
		//
		// // Page.getCurrent().setUriFragment("!Map/" + s);
		// } else if (name.equals("Island")) {
		//
		// Location loc = dao.find(Location.class, i);
		//
		// ibisMap.setLocation(loc);
		// }
		//
		// }
		//
		// @Override
		// public void onSelect(ResourceDescription facetedSearch) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		// tabSheet.addTab(searchPanel, "Search");

		// this.speciesSummaryView = new SpeciesSummaryView();
		// tabSheet.addTab(speciesSummaryView, "Species");
		// addSpeciesInfo(Species.INVASIVE, "Invasive");
		// addSpeciesInfo(Species.THREATENED, "Threatened");

		// tabSheet.addTab(new Label(""), "Island");

	}
	

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	// private void addSpeciesInfo(String invasive, String caption) {
	//
	// CssLayout l = new CssLayout();
	// tabSheet.addTab(l, "Species");
	//
	// EntityManager em = dao.getEntityManager();
	// TypedQuery<Species> q = em.createNamedQuery(invasive, Species.class);
	// List<Species> res = q.getResultList();
	//
	// // NativeSelect combo = new NativeSelect("Invasive");
	// ComboBox combo = new ComboBox(caption);
	// combo.setWidth(FIELDWIDTH);
	// l.addComponent(combo);
	//
	// for (Species species : res) {
	// combo.addItem(species);
	// }
	//
	// }
}