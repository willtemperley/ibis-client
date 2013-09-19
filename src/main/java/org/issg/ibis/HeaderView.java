package org.issg.ibis;

import org.issg.ibis.client.SpeciesSelector;

import com.google.inject.Inject;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class HeaderView extends HorizontalLayout implements View {

    private Window popupWindow;

    @Inject
    public HeaderView(AccountOptionView menuView,
            SpeciesSelector speciesSelector) {

        this.setStyleName("header");
        this.setSizeFull();

        Label label = new Label();
        label.setContentMode(ContentMode.HTML);

        String text = "<div><div class='ibis'>ibis</div>"
                + "<div class='ibis-text'>island biodiversity<br/>&amp; invasive species</div></div>";

        label.setValue(text);

        addComponent(label);

        addMenu();

        Label logoLabel = new Label();
        logoLabel.setContentMode(ContentMode.HTML);
        logoLabel
                .setValue("<div class='logo-container'><div class='issg-logo'></div><div class='ec-logo'></div><div class='iucn-logo'></div></div>");

        addComponent(logoLabel);

        {

            popupWindow = new Window();
            popupWindow.setModal(true);
            popupWindow.setWidth("728px");
            popupWindow.setHeight("800px");
            popupWindow.setContent(speciesSelector);
        }

    }

    private void addMenu() {

        {
            final TextField t = new TextField();
            Button b = new Button("Search");
            HorizontalLayout hl = new HorizontalLayout();
            hl.addComponent(t);
            hl.addComponent(b);
            hl.addStyleName("search-box");
            addComponent(hl);

            b.addClickListener(new Button.ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    getUI().getNavigator().navigateTo("Home/" + t.getValue());
                }
                
            });
            // Button sp = new Button("Species");
            // addComponent(sp);
            //
            // sp.addClickListener(new Button.ClickListener() {
            //
            // @Override
            // public void buttonClick(ClickEvent event) {
            // showSpeciesMenu();
            // }
            // });
        }

        {
            // Button sp = new Button("Locations");
            // addComponent(sp);
            // sp.addClickListener(new Button.ClickListener() {
            //
            // @Override
            // public void buttonClick(ClickEvent event) {
            // showLocationSearch();
            // }
            // });

        }

    }

    private void showLocationSearch() {
        popupWindow.setContent(new Label("X"));
        getUI().addWindow(popupWindow);
    }

    private void showSpeciesMenu() {
        popupWindow.center();
        getUI().addWindow(popupWindow);

        Navigator nav = getUI().getNavigator();
        if (nav == null)
            Notification.show("null nav");

        nav.addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {
                popupWindow.close();
            }

        });
    };

    @Override
    public void enter(ViewChangeEvent event) {

    }

}
