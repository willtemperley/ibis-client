package org.issg.ibis;

import it.jrc.auth.RoleManager;

import org.jrc.auth.domain.Role;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;

public class HeaderView extends HorizontalLayout implements View {

    private AccountDetails accountDetails;
    private String contextPath;

    @Inject
    public HeaderView(AccountOptionView menuView,
            @Named("context_path") String contextPath, RoleManager roleManager) {

        this.setStyleName("header");
        this.setSizeFull();

        this.contextPath = contextPath;

        addLogo();
        addHomeLink();

        accountDetails = new AccountDetails(roleManager);
        addComponent(accountDetails);

        addPartnerLogos();

        accountDetails.setRole(roleManager.getRole());
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
                    roleManager.logout();
                    setRole(roleManager.getRole());
                    Notification.show("You have been logged out.");
                }
            });

            addComponent(settings);
            label = new Label();
            addComponent(label);
            
            setRole(roleManager.getRole());
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
        Label label = new Label();
        label.setContentMode(ContentMode.HTML);

        String text = "<div><div class='ibis'>ibis</div>"
                + "<div class='ibis-text'>island biodiversity<br/>&amp; invasive species</div></div>";

        label.setValue(text);

        addComponent(label);
    }

    private void addHomeLink() {
        ExternalResource r = new ExternalResource("");
        addComponent(new Link("Home", r));
    }

    // private void addSearchBox() {
    //
    // HorizontalLayout hl = new HorizontalLayout();
    //
    // final TextField t = new TextField();
    //
    // final Button b = FontelloButtonMaker
    // .getButton(FontelloButtonMaker.ButtonIcon.icon_search);
    //
    // hl.addComponent(t);
    // hl.addComponent(b);
    // hl.addStyleName("search-box");
    //
    // // silly wrapper to make full size then centre the hl
    // HorizontalLayout wrapper = new HorizontalLayout();
    // wrapper.setSizeFull();
    // wrapper.addComponent(hl);
    // wrapper.setComponentAlignment(hl, Alignment.MIDDLE_CENTER);
    //
    // addComponent(wrapper);
    //
    // b.addClickListener(new Button.ClickListener() {
    //
    // @Override
    // public void buttonClick(ClickEvent event) {
    // getUI().getNavigator().navigateTo("Search/" + t.getValue());
    // }
    //
    // });
    //
    // /*
    // * Add and remove click shortcuts
    // */
    // t.addFocusListener(new FieldEvents.FocusListener() {
    // @Override
    // public void focus(FocusEvent event) {
    // b.setClickShortcut(KeyCode.ENTER);
    // }
    // });
    //
    // t.addBlurListener(new FieldEvents.BlurListener() {
    // @Override
    // public void blur(BlurEvent event) {
    // b.removeClickShortcut();
    // }
    // });
    //
    // }

    private void addPartnerLogos() {
        Label logoLabel = new Label();
        logoLabel.setContentMode(ContentMode.HTML);
        logoLabel
                .setValue("<div class='logo-container'><div class='issg-logo'></div><div class='ec-logo'></div><div class='iucn-logo'></div></div>");

        addComponent(logoLabel);
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

}
