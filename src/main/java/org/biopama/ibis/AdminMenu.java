package org.biopama.ibis;

import org.biopama.ibis.auth.RoleManager;
import org.biopama.ibis.auth.RoleManager.Action;

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

	/**
	 * Adds an item to
	 * @return 
	 */

	public MenuItem addAdminItem(Class<?> clazz, String caption, final String location) {
		return addAdminItem(item, clazz, caption, location);
	}
	
	public MenuItem getRootItem() {
		return item;
	}

	public MenuItem addAdminItem(MenuItem parentItem, Class<?> clazz, String caption, final String location) {
		if (roleManager.getActionsForTarget(clazz).contains(Action.READ)) {
			hasItems = true;
			return parentItem.addItem(caption, new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					UI.getCurrent().getNavigator().navigateTo(location);
				}
			});
		}
		return parentItem;
	}

	public boolean hasItems() {
		return hasItems;
	}
}
