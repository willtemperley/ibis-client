package org.issg.ibis.responsive;

import org.issg.ibis.NavMenu;
import org.issg.ibis.ViewModule;
import org.jrc.edit.RoleManager;
import org.vaadin.addons.auth.domain.Role;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.BaseTheme;

public class HeaderView extends HorizontalLayout implements View {

    private AccountDetails accountDetails;
    private String contextPath;
	private NavMenu navMenu;

    @Inject
    public HeaderView(NavMenu navMenu, @Named("context_path") String contextPath, RoleManager roleManager) {
    	
        this.setStyleName("header");
        this.setSizeFull();

//        this.contextPath = contextPath;
//
//        addLogo();
        
        Button b = new Button("IBIS");
        b.addStyleName("ibis");
        b.addStyleName(BaseTheme.BUTTON_LINK);
        addComponent(b);
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

        setExpandRatio(label, 1);
    }

    private class AccountDetails extends HorizontalLayout {

        private Label label;
        private Link loginLink;
        private MenuBar settings;

        public AccountDetails(final RoleManager roleManager) {

            loginLink = getLoginLink(contextPath);
            addComponent(loginLink);

            settings = new MenuBar();
            MenuItem settingsMenu = settings.addItem("", null);
            settingsMenu.setStyleName("icon-cog");
            settingsMenu.addItem("Issues", new Command() {
                @Override
                public void menuSelected(MenuItem selectedItem) {
                    getUI().getNavigator().navigateTo(ViewModule.ISSUE_EDITOR);
                }
            });

            settingsMenu.addSeparator();
            settingsMenu.addItem("Logout", new Command() {
                @Override
                public void menuSelected(MenuItem selectedItem) {
//                    roleManager.logout();
//                    setRole(roleManager.getRole());
//                    Notification.show("You have been logged out.");
                }
            });

            addComponent(settings);
            label = new Label();
            addComponent(label);
            
//            setRole(roleManager.getRole());
        }

        private void setRole(Role role) {

            boolean showSettings;
            if (role == null) {
                showSettings = false;
            } else {
                showSettings = ! role.isAnonymous();
            }

            settings.setVisible(showSettings);
            label.setVisible(showSettings);
            loginLink.setVisible(!showSettings);

            label.setValue(role.toString());

        }
        

        private Link getLoginLink(String contextPath) {
            ExternalResource r = new ExternalResource(contextPath
                    + "/login?action=change");
            Link link = new Link("Login", r);
//            link.setTargetName("_blank");
            return link;
        }

    }


    private void addLogo() {
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
