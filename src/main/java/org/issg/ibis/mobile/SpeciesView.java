package org.issg.ibis.mobile;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.NumberField;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;

public class SpeciesView extends NavigationView {

	public SpeciesView() {
		super("Planet Details");

		CssLayout content = new CssLayout();
		setContent(content);

		VerticalComponentGroup group = new VerticalComponentGroup();
		content.addComponent(group);

		group.addComponent(new TextField("Planet"));
		group.addComponent(new NumberField("Found"));
		group.addComponent(new Switch("Probed"));

		setRightComponent(new Button("OK"));
	}

}
