package org.issg.ibis.qdsl.search;

import java.util.List;

import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.NativeSelect;

public class TypedSelect<T> extends ComboBox {
	
	public interface ValueChangeListener<X> {
		public void onValueChange(X val);
	}

	public TypedSelect(String string) {
		super(string);
	}

	public void addItems(List<T> items) {
		for (T t : items) {
			addItem(t);
		}
	}

	public void addVCL(final ValueChangeListener<T> vcl) {
		
		addValueChangeListener(new Property.ValueChangeListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				vcl.onValueChange((T) event.getProperty().getValue());
			}
		});
	}
	

}
