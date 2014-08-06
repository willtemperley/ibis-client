package org.vaadin.addons.form.view;

import java.util.List;
import java.util.Set;

import org.issg.ibis.auth.RoleManager.Action;
import org.vaadin.addons.form.field.FieldGroup;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class TwinPanelEditorView<T> extends HorizontalLayout implements IEditorView<T>  {

    private SubmitPanel submitPanel = new SubmitPanel();
	private Panel leftPlaceholder = new Panel();
	private VerticalLayout formLayout = new VerticalLayout();
	private Panel rightPanel;
	private VerticalLayout formVL;
	private VerticalLayout createVL;
	private Button createButton;
    
    public TwinPanelEditorView() {
    	
    	setSpacing(true);
		addComponent(leftPlaceholder);
		submitPanel.setWidth("100%");
		
		formLayout.setSpacing(true);
		
    	rightPanel = new Panel();
		rightPanel.setSizeFull();
		addComponent(rightPanel);

		createButton = new Button("Create");
		createVL = new VerticalLayout();
		createVL.setSizeFull();
		createVL.addComponent(createButton);
		createVL.setComponentAlignment(createButton, Alignment.MIDDLE_CENTER);
		rightPanel.setContent(createVL);

		formVL = new VerticalLayout();
		formLayout.setSizeUndefined();
//		formVL.setSizeFull();
		formVL.addComponent(formLayout);
		formVL.addComponent(submitPanel);
		
        setSizeFull();
    }


    @Override
    public void buildForm(List<FieldGroup<T>> fields) {
        for (FieldGroup<T> fieldGroup : fields) {
            for (Field<?> field : fieldGroup.getFieldGroup().getFields()) {
                formLayout.addComponent(field);
            }
        }
        
//        formLayout.addComponent(submitPanel);
    }


	public void setSelectionComponent(Panel panel) {
		replaceComponent(leftPlaceholder, panel);
	}


	@Override
	public void setAllowedActions(Set<Action> allowedActions) {
		submitPanel.setAllowedActions(allowedActions);
//		createButton.setEnabled(allowedActions.contains(Action.CREATE));
	}


	@Override
	public void setIsEditing(boolean isEditing) {
		//Could this be removed by figuring out the state as given by the view?

		if (isEditing) {
			rightPanel.setContent(formVL);
		} else {
			rightPanel.setContent(createVL);
		}

	}

	@Override
	public void setUpdateListener(ClickListener clickListener) {
		submitPanel.getSaveButton().addClickListener(clickListener);
	}

	@Override
	public void setCancelListener(ClickListener clickListener) {
	}

	@Override
	public void setDeleteListener(ClickListener clickListener) {
		submitPanel.getDeleteButton().addClickListener(clickListener);
	}

	@Override
	public void setCreateListener(ClickListener clickListener) {
		createButton.addClickListener(clickListener);
	}
}
