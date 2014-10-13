package org.issg.ibis.mobile;

import com.google.inject.Inject;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.NumberField;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;

public class LocationView extends NavigationView {

	@Inject
	public LocationView() {
		super("Location Details");

		CssLayout content = new CssLayout();
		setContent(content);

		VerticalComponentGroup group = new VerticalComponentGroup();
		content.addComponent(group);

		group.addComponent(new TextField("Planet"));
		group.addComponent(new NumberField("Found"));
		group.addComponent(new Switch("Probed"));

		Button c = new Button("OK");
		setRightComponent(c);
		c.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
//				navMan.navigateTo(SpeciesView.class);
			}
		});
	}

}
