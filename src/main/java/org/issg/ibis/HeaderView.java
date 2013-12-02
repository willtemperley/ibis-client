package org.issg.ibis;

import org.issg.ibis.client.SpeciesSelector;
import org.jrc.ui.FontelloButtonMaker;

import com.google.inject.Inject;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TextField;
import elemental.events.KeyboardEvent.KeyCode;

public class HeaderView extends HorizontalLayout implements View {

    @Inject
    public HeaderView(AccountOptionView menuView,
            SpeciesSelector speciesSelector) {

        this.setStyleName("header");
        this.setSizeFull();

        addLogo();

        addSearchBox();

        addPartnerLogos();

    }

    private void addLogo() {
        Label label = new Label();
        label.setContentMode(ContentMode.HTML);

        String text = "<div><div class='ibis'>ibis</div>"
                + "<div class='ibis-text'>island biodiversity<br/>&amp; invasive species</div></div>";

        label.setValue(text);

        addComponent(label);
    }

    private void addSearchBox() {

        HorizontalLayout hl = new HorizontalLayout();
        
        final TextField t = new TextField();

        final Button b = FontelloButtonMaker
                .getButton(FontelloButtonMaker.ButtonIcon.icon_search);
        
        hl.addComponent(t);
        hl.addComponent(b);
        hl.addStyleName("search-box");

        //silly wrapper to make full size then centre the hl
        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.setSizeFull();
        wrapper.addComponent(hl);
        wrapper.setComponentAlignment(hl, Alignment.MIDDLE_CENTER);
        
        
        addComponent(wrapper);

        b.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                getUI().getNavigator().navigateTo("Search/" + t.getValue());
            }

        });

        /*
         * Add and remove click shortcuts
         */
        t.addFocusListener(new FieldEvents.FocusListener() {
            @Override
            public void focus(FocusEvent event) {
                b.setClickShortcut(KeyCode.ENTER);
            }
        });

        t.addBlurListener(new FieldEvents.BlurListener() {
            @Override
            public void blur(BlurEvent event) {
                b.removeClickShortcut();
            }
        });

    }

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
