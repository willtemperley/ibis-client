package org.issg.ibis.mobile;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;

public class GuicedNavigationManager extends NavigationManager {

	Map<Class<?>, NavigationView> viewMap = new HashMap<Class<?>, NavigationView>();

	@Inject
	public GuicedNavigationManager(SpeciesView sv, LocationView lv) {
		super(lv);
		viewMap.put(SpeciesView.class, sv);
		viewMap.put(LocationView.class, lv);
	}
	
//	public void getInitialView() {
//		return viewMap.get(SpeciesView.class)
//	}
	
	public void navigateTo(Class<? extends NavigationView> clazz) {
		
		super.navigateTo(viewMap.get(clazz));

	}

}
