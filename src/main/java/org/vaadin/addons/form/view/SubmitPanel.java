package org.vaadin.addons.form.view;

import java.util.Set;

import org.issg.ibis.auth.RoleManager.Action;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;


public class SubmitPanel extends CssLayout {
    
    
    private Button commit;
	private Button delete;

	public SubmitPanel() {
    	setStyleName("submit-panel");
        commit = new Button("Save", FontAwesome.FLOPPY_O);
        commit.setStyleName(ValoTheme.BUTTON_PRIMARY);
        commit.addStyleName("button-left");
        addComponent(commit);
        delete = new Button("Delete", FontAwesome.TIMES);
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
    

}
