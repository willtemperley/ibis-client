package org.biopama.ibis.editor.view;

import java.util.Set;

import org.biopama.ibis.auth.RoleManager.Action;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.CssLayout;


public class SubmitPanel extends CssLayout {
    
    
    private Button commit;
	private Button delete;
	private Button cancel;

	public SubmitPanel() {
    	setStyleName("submit-panel");
        commit = new Button("Save", FontAwesome.FLOPPY_O);
        commit.setStyleName(ValoTheme.BUTTON_PRIMARY);
        commit.addStyleName("button-left");
        addComponent(commit);
        cancel = new Button("Cancel", FontAwesome.TIMES);
        cancel.addStyleName("button-cancel");
        addComponent(cancel);
        delete = new Button("Delete", FontAwesome.TRASH_O);
        delete.setStyleName(ValoTheme.BUTTON_DANGER);
        delete.addStyleName("button-right");
        addComponent(delete);
    }


    

	public void setAllowedActions(Set<Action> allowedActions) {

		commit.setEnabled(allowedActions.contains(Action.UPDATE));
		delete.setEnabled(allowedActions.contains(Action.DELETE));
		
	}


	public Button getSaveButton() {
		return commit;
	}

	public Button getDeleteButton() {
		return delete;
	}

	public Button getCancelButton() {
		return cancel;
	}
    

}
