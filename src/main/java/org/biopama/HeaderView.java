package org.biopama;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.issg.ibis.AccountDetails;
import org.issg.ibis.AdminMenu;
import org.issg.ibis.NavMenu;
import org.issg.ibis.ViewModule;
import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.domain.BiologicalStatus;
import org.issg.ibis.domain.ConservationClassification;
import org.issg.ibis.domain.ImpactMechanism;
import org.issg.ibis.domain.ImpactOutcome;
import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.OrganismType;
import org.issg.ibis.domain.Reference;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.vaadin.addons.auth.domain.Role;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.BaseTheme;

public class HeaderView extends HorizontalLayout implements View {

	private NavMenu navMenu;

    @Inject
    public HeaderView(AccountDetails accountDetails, RoleManager roleManager) {
    	
        this.setStyleName("banner");
        this.setSizeFull();

        Button b = new Button("IBIS");
        b.addStyleName("ibis");
        b.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        addComponent(b);
        b.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				UI.getCurrent().getNavigator().navigateTo("");

			}
		});

        Label label = new Label();
        label.setContentMode(ContentMode.HTML);
        String text = "<div class='ibis-text'><span class='bio-text'>island biodiversity</span>&nbsp;<span class='inv-text'>invasive species</span></div>";
        label.setValue(text);
        addComponent(label);

        MenuBar mb = new MenuBar();
        mb.addStyleName("biopama-menu");
        mb.addItem("ABOUT", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuItem selectedItem) {
                UI.getCurrent().getNavigator().navigateTo(ViewModule.SEARCH);
            }
        });
        mb.addItem("SEARCH", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuItem selectedItem) {
                UI.getCurrent().getNavigator().navigateTo(ViewModule.SEARCH);
            }
        });
        mb.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
//		mb.setWidth("100%");
        addComponent(mb);

        AdminMenu adminMenu = new AdminMenu(roleManager);
        adminMenu.addAdminItem(Species.class, "Species", ViewModule.SPECIES_EDITOR);
        adminMenu.addAdminItem(Location.class, "Location", ViewModule.LOCATION_EDITOR);

        //Can upload species impact in this case
        adminMenu.addAdminItem(SpeciesImpact.class, "Upload", ViewModule.UPLOAD);

        adminMenu.addAdminItem(Role.class, "Users", ViewModule.USER_EDITOR);
        
        MenuItem x = adminMenu.getRootItem().addItem("Lookup tables", null);
        adminMenu.addAdminItem(x, BiologicalStatus.class, "Biological Status", ViewModule.BIOLOGICAL_STATUS);
        adminMenu.addAdminItem(x, ConservationClassification.class, "Cons. Classification", ViewModule.CONSERVATION_CLASSIFICATION);
        adminMenu.addAdminItem(x, ImpactMechanism.class, "Impact Mechanism", ViewModule.IMPACT_MECHANISM);
        adminMenu.addAdminItem(x, ImpactOutcome.class, "Impact Outcome", ViewModule.IMPACT_OUTCOME);
        adminMenu.addAdminItem(x, OrganismType.class, "Organism Type", ViewModule.ORGANISM_TYPE);
        adminMenu.addAdminItem(x, Reference.class, "Reference", ViewModule.REFERENCE);


        if (adminMenu.hasItems()) {
        	addComponent(adminMenu);
		}
        
		addComponent(accountDetails);
        setExpandRatio(label, 1);
    }


    @Override
    public void enter(ViewChangeEvent event) {

    }
}
