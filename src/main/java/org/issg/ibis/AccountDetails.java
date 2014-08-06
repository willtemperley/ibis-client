package org.issg.ibis;

import com.google.inject.Inject;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

class AccountDetails extends Button {

	private AccountOptionWindow authenticationOptions;

	@Inject
	public AccountDetails(AccountOptionWindow window) {
		
		addStyleName("account-settings");
		
		setIcon(FontAwesome.BARS);
		
		this.authenticationOptions = window;

		
		addClickListener(new ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {

            event.getButton().setDescription("Account options");

            if (authenticationOptions != null && authenticationOptions.getUI() != null)
                authenticationOptions.close();
            else {
                authenticationOptions.show(getUI(), event);

                ((VerticalLayout) getUI().getContent()).addLayoutClickListener(new LayoutClickListener() {
                            @Override
                            public void layoutClick(LayoutClickEvent event) {
                                authenticationOptions.close();
                                ((VerticalLayout) getUI().getContent())
                                        .removeLayoutClickListener(this);
                            }
                        });
            }

        }
    });
	}
	
	

}