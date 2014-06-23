package org.issg.ibis.perspective.species;

import org.issg.ibis.domain.Species;
import org.jrc.server.lec.ListContainer.DynaBeanItem;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class Taxonomy extends FormLayout {
	
	private Label kingdom = new Label();
	private Label phylum = new Label();
	private Label clazz = new Label();
	private Label order = new Label();
	private Label family = new Label();
//	private Label genus = new Label();
	
	public Taxonomy() {
		kingdom.setCaption("Kingdom");
		phylum.setCaption("Phylum");
		clazz.setCaption("Class");
		order.setCaption("Order");
		family.setCaption("Family");
//		genus.setCaption("Genus");
		
		addComponent(kingdom);
		addComponent(phylum);
		addComponent(clazz);
		addComponent(order);
		addComponent(family);
//		addComponent(genus);
	}
	
	public void setSpecies(Species sp) {

		kingdom.setValue(sp.getKingdom());
		phylum.setValue(sp.getPhylum());
		clazz.setValue(sp.getClazz());
		order.setValue(sp.getOrder());
		family.setValue(sp.getFamily());
//		genus.setValue(sp.getGenus());
	}

}
