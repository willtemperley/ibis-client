package org.vaadin.addons.form.view;

import java.util.List;

import org.vaadin.addons.form.field.FieldGroup;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Field;

public class DefaultEditorView<T> extends CssLayout implements IEditorView<T>  {

    SubmitPanel submitPanel = new SubmitPanel();
    
    public DefaultEditorView() {
        addStyleName("layout-panel");
        setSizeFull();
    }


    @Override
    public SubmitPanel getSubmitPanel() {
        return submitPanel;
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

}
