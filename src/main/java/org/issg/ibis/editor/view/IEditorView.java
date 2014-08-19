package org.issg.ibis.editor.view;

import java.util.List;
import java.util.Set;

import org.issg.ibis.auth.RoleManager.Action;
import org.vaadin.addons.form.field.FieldGroup;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;

/**
 * Form views should implement this.  This allows views to be "throwaway" components.
 * 
 * @author Will Temperley
 * 
 */
public interface IEditorView<T> extends Component {


    /**
     * Add fields to form. Implementations are expected to only manage the
     * display, not manipulate form values.
     * 
     * @param fields
     */
    public void buildForm(List<FieldGroup<T>> fields);

    /**
     * 
     * 
     * @param allowedActions
     */
	public void setAllowedActions(Set<Action> allowedActions);
	
	public void setIsEditing(boolean isEditing);

	public void setCreateListener(ClickListener clickListener);

	public void setUpdateListener(ClickListener clickListener);

	public void setCancelListener(ClickListener clickListener);

	public void setDeleteListener(ClickListener clickListener);
		
}
