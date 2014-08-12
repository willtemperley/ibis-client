package org.issg.ibis;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.auth.RoleManager.Action;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.Species;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.BaseTheme;

public class HeaderView extends HorizontalLayout implements View {

	private NavMenu navMenu;

    @Inject
    public HeaderView(AccountDetails accountDetails, RoleManager roleManager) {
    	
        this.setStyleName("header");
        this.setSizeFull();

        Button b = new Button("IBIS");
        b.addStyleName("ibis");
        b.addStyleName(BaseTheme.BUTTON_LINK);
        addComponent(b);
//        label.setWidth("60px");
        b.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				UI.getCurrent().getNavigator().navigateTo("");;

			}
		});

        Label label = new Label();
        label.setContentMode(ContentMode.HTML);
        String text = "<div class='ibis-text'>island biodiversity &amp; invasive species</div>";
        label.setValue(text);
        addComponent(label);

        AdminMenu adminMenu = new AdminMenu(roleManager);
        adminMenu.addAdminItem(Species.class, "Species", ViewModule.SPECIES_EDITOR);
        adminMenu.addAdminItem(Location.class, "Location", ViewModule.LOCATION_EDITOR);
        if (adminMenu.hasItems()) {
        	addComponent(adminMenu);
		}
        
		addComponent(accountDetails);
        setExpandRatio(label, 1);
    }

    private void addPartnerLogos() {
        Label logoLabel = new Label();
        logoLabel.setContentMode(ContentMode.HTML);
        logoLabel.setValue("<div class='logo-container'><div class='issg-logo'></div><div class='ec-logo'></div><div class='iucn-logo'></div></div>");

        addComponent(logoLabel);
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }
    
    public NavMenu getNavMenu() {
		return navMenu;
	}

}
