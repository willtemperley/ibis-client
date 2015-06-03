package org.biopama.ibis;

import org.biopama.ibis.auth.RoleManager;
import org.vaadin.addons.guice.uiscope.UIScoped;
import org.vaadin.addons.oauth.OAuthManager;
import org.vaadin.addons.oauth.OAuthRequestHandler;
import org.vaadin.addons.oauth.UserInfo;
import org.vaadin.maddon.button.MButton;

import com.google.inject.Inject;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@UIScoped
public class AccountOptionWindow extends Window {

	private static final String _300PX = "300px";
	private Label label;
	private RoleManager roleManager;
	private OAuthRequestHandler rh;

	public void show(UI ui, ClickEvent event) {
        setPositionX(event.getClientX() - event.getRelativeX()-300);
        setPositionY(event.getClientY() - event.getRelativeY()+40);
        ui.addWindow(this);
        this.focus();
	}

	@Inject
	public AccountOptionWindow(final OAuthManager oauthManager, RoleManager roleManager, OAuthRequestHandler rh) {

        setClosable(false);
        setResizable(false);
        setDraggable(false);
        setCloseShortcut(KeyCode.ESCAPE, null);
        setWidth(_300PX);
        
        this.roleManager = roleManager;
        this.rh = rh;

		if (!roleManager.isLoggedIn()) {
			buildLoggedOutInterface(oauthManager);
			VaadinSession.getCurrent().removeRequestHandler(this.rh);

		} else {
			buildLoggedInInterface();
		}

	}
	
	private void buildLoggedInInterface() {
		UserInfo userInfo = roleManager.getUserInfo();
		Panel p = new Panel();
        setContent(p);
		VerticalLayout l = new VerticalLayout();
		p.setContent(l);
        l.setMargin(true);
        l.setSpacing(true);

        p.setCaption(userInfo.getEmail());
        
        Image c = new Image(null, new ExternalResource(userInfo.getPicture()));
        c.setWidth("100px");
        c.setHeight("100px");
		l.addComponent(c);
        MButton logoutButton = new MButton("Logout").withStyleName("default");
		l.addComponent(logoutButton);
		
		logoutButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				roleManager.logout();
				UI.getCurrent().getPage().setLocation("");
			}
		});
		

	}

	private void buildLoggedOutInterface(final OAuthManager oauthManager) {
		VerticalLayout l = new VerticalLayout();
        l.setMargin(true);
        l.setSpacing(true);
        setContent(l);
        addStyleName("notifications");

        label = new Label("Choose OAuth provider:", ContentMode.HTML);
        l.addComponent(label);
        Button b = new Button("Sign in with Google");
        b.addStyleName("g-plus-button");
        l.addComponent(b);
		Resource icon = new ThemeResource("img/oauth/g-plus.png");
		b.setIcon(icon);

		b.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				final String authenticationUrl = oauthManager.buildLoginUrl();
//				// redirect to provider
				UI.getCurrent().getPage().setLocation(authenticationUrl);
				VaadinSession.getCurrent().addRequestHandler(rh);
				
			}
		});
	}
		


	public void setEmail(String email) {
		label.setValue(email);
	}

	public void setUserInfo(UserInfo userInfo) {
		if(userInfo == null) {
			
		}
	}

}
