package org.issg.ibis.editor;

import java.util.List;

import org.issg.ibis.domain.Species;
import org.vaadin.addons.form.field.FieldGroup;
import org.vaadin.addons.form.view.DefaultEditorView;

import com.vaadin.ui.Button;

public class InlineEditSpeciesView extends DefaultEditorView<Species>{

	Button button = new Button("Populate");

	public InlineEditSpeciesView() {
		super();

	}
	
	public Button getButton() {
		return button;
	}
	
	@Override
	public void buildForm(List<FieldGroup<Species>> fields) {
		
		addComponent(button);

		
		super.buildForm(fields);
	}

}
