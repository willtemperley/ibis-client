package org.issg.ibis.editor;

import org.issg.ibis.domain.Species;
import org.jrc.edit.EditorController;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class SpeciesToolBar extends MenuBar {

	final EditorController<Species> parentController;

	public SpeciesToolBar(EditorController<Species> parentController) {
		this.parentController = parentController;
	}
	
	public void addButton(final String buttonCaption, final ISpeciesEditor editor) {
		
		addItem(buttonCaption, new Command() {
			
			@Override
			public void menuSelected(MenuItem selectedItem) {

				Window window = new Window();
				window.setContent(editor);
				Species sp = parentController.getEntity();
				if (sp.getId() == null) {
					Notification.show("Please save species to database before modifying " + buttonCaption);
					return;
				}
				editor.setSpecies(parentController.getEntity());
				window.setSizeFull();
				window.setCaption(editor.getCaption());
				UI.getCurrent().addWindow(window);
				
			}
		});

	}
}
