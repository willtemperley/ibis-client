package org.issg.ibis;

import org.issg.ibis.auth.RoleManager;
import org.vaadin.addons.auth.domain.Role;
import org.vaadin.addons.oauth.OAuthManager;

import com.google.inject.Inject;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

class AccountDetails extends Button {

	private AccountOptionWindow notifications;



	@Inject
	public AccountDetails(final RoleManager roleManager, AccountOptionWindow window) {

		addStyleName("account-settings");
		addStyleName("icon-only");
		
		setIcon(new ThemeResource("img/menu.png"));
		
		this.notifications = window;
//		// Blank item to be styled TODO
//		MenuItem settingsMenu = addItem("", null);
//
//
//		MenuItem item = settingsMenu.addItem("Login", new Command() {
//			@Override
//			public void menuSelected(MenuItem selectedItem) {
//
//
//			}
//		});
//
//		settingsMenu.addSeparator();
//		settingsMenu.addItem("Logout", new Command() {
//			@Override
//			public void menuSelected(MenuItem selectedItem) {
//				// roleManager.logout();
//				// setRole(roleManager.getRole());
//				// Notification.show("You have been logged out.");
//			}
//		});

		addClickListener(new ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {

//            event.getButton().setDescription("Notifications");

            if (notifications != null && notifications.getUI() != null)
                notifications.close();
            else {
                notifications.show(getUI(), event);

                ((VerticalLayout) getUI().getContent()).addLayoutClickListener(new LayoutClickListener() {
                            @Override
                            public void layoutClick(LayoutClickEvent event) {
                                notifications.close();
                                ((VerticalLayout) getUI().getContent())
                                        .removeLayoutClickListener(this);
                            }
                        });
            }

        }
    });
	}
	
	
	
//    private void buildNotifications(ClickEvent event) {
//        notifications = new Window("Not logged in.");
//        VerticalLayout l = new VerticalLayout();
//        l.setMargin(true);
//        l.setSpacing(true);
//        notifications.setContent(l);
//        notifications.setWidth("300px");
//        notifications.addStyleName("notifications");
//        notifications.setClosable(false);
//        notifications.setResizable(false);
//        notifications.setDraggable(false);
//        notifications.setPositionX(event.getClientX() - event.getRelativeX()-300);
//        notifications.setPositionY(event.getClientY() - event.getRelativeY()-50);
//        notifications.setCloseShortcut(KeyCode.ESCAPE, null);
//
//        Label label = new Label("Choose OAuth provider:", ContentMode.HTML);
//        l.addComponent(label);
//        Button b = new Button("Sign in with Google");
//        b.addStyleName("g-plus-button");
//        l.addComponent(b);
//		Resource icon = new ThemeResource("img/oauth/g-plus.png");
//		b.setIcon(icon);
//
//		b.addClickListener(new ClickListener() {
//			
//			@Override
//			public void buttonClick(ClickEvent event) {
//				// TODO Auto-generated method stub
//				final String authenticationUrl = oauthManager.buildLoginUrl();
////				// redirect to provider
//				UI.getCurrent().getPage().setLocation(authenticationUrl);
//				
//			}
//		});
//		
//    }


}