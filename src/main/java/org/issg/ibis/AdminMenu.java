package org.issg.ibis;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.auth.RoleManager.Action;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;

public class AdminMenu extends MenuBar {
	
	RoleManager roleManager;
	private MenuItem item;
	private boolean hasItems;
	
	public AdminMenu(RoleManager roleManager) {
		addStyleName("admin-menu");
		this.roleManager = roleManager;
		item = addItem("", null);
		item.setIcon(FontAwesome.COG);
		hasItems = false;
	}

	public void addAdminItem(Class<?> clazz, String caption, final String location) {
		if (roleManager.getActionsForTarget(clazz).contains(Action.UPDATE)) {
			hasItems = true;
			item.addItem(caption, new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					UI.getCurrent().getNavigator().navigateTo(location);
				}
			});
		}
		
	}

	public boolean hasItems() {
		return hasItems;
	}
}
