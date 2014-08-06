package org.vaadin.addons.form.view;

import java.util.List;
import java.util.Set;

import org.issg.ibis.auth.RoleManager.Action;
import org.vaadin.addons.form.field.FieldGroup;
import org.vaadin.addons.lec.EntityTable;

import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Field;

public class DefaultEditorView<T> extends CssLayout implements IEditorView<T>  {

    SubmitPanel submitPanel = new SubmitPanel();
    
    public DefaultEditorView() {
        addStyleName("layout-panel");
        setSizeFull();
    }



    @Override
    public void buildForm(List<FieldGroup<T>> fields) {
        for (FieldGroup<T> fieldGroup : fields) {
            for (Field<?> field : fieldGroup.getFieldGroup().getFields()) {
                addComponent(field);
            }
        }
        
        addComponent(submitPanel);
    }

	@Override
	public void setAllowedActions(Set<Action> allowedActions) {
		submitPanel.setAllowedActions(allowedActions);
	}


	@Override
	public void setIsEditing(boolean isEditing) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void setCreateListener(ClickListener clickListener) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void setUpdateListener(ClickListener clickListener) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void setCancelListener(ClickListener clickListener) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void setDeleteListener(ClickListener clickListener) {
		// TODO Auto-generated method stub
		
	}

}
