package org.issg.ibis;

import org.vaadin.addons.guice.uiscope.UIScoped;
import org.vaadin.addons.oauth.OAuthManager;

import com.google.inject.Inject;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@UIScoped
public class AccountOptionWindow extends Window {

	public void show(UI ui, ClickEvent event) {
        setPositionX(event.getClientX() - event.getRelativeX()-300);
        setPositionY(event.getClientY() - event.getRelativeY()-50);
        ui.addWindow(this);
        this.focus();
	}

	@Inject
	public AccountOptionWindow(final OAuthManager oauthManager) {

        VerticalLayout l = new VerticalLayout();
        l.setMargin(true);
        l.setSpacing(true);
        setContent(l);
        setWidth("300px");
        addStyleName("notifications");
        setClosable(false);
        setResizable(false);
        setDraggable(false);
        setCloseShortcut(KeyCode.ESCAPE, null);

        Label label = new Label("Choose OAuth provider:", ContentMode.HTML);
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
				
			}
		});
	}
		

	public void tempSetStuff(String e) {
		this.setContent(new Label(e));
	}

}
