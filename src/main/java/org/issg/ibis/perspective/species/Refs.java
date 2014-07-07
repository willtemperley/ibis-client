package org.issg.ibis.perspective.species;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.issg.ibis.domain.Reference;
import org.jrc.ui.HtmlHeader;
import org.jrc.ui.HtmlLabel;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

public class Refs extends CssLayout {

    private List<Component> components = new ArrayList<Component>();

	public Refs() {
        HtmlHeader header = new HtmlHeader("References");
        addComponent(header);
	}
	
	public void setReferences(Set<Reference> refs) {
		if (refs == null || refs.size() == 0) {
			setVisible(false);
			return;
		}
		
		if (!isVisible()) {
			setVisible(true);
		}
		
		for (Component c : components) {
			removeComponent(c);
		}

		for (Reference reference : refs) {
			HtmlLabel c = new HtmlLabel(reference.getContent());
			addComponent(c);
			components.add(c);
		}
	}

}
