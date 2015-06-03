package org.biopama.ibis.editor;

import com.vaadin.ui.ComboBox;

public class CommonNameCombo extends ComboBox {
	
	public CommonNameCombo() {
		this.setNewItemsAllowed(true);
	}

	@Override
	protected void setInternalValue(Object newValue) {
		if (newValue != null && !items.containsId(newValue)) {
			addItem(newValue);
		}
		super.setInternalValue(newValue);
	}
	
	public CommonNameCombo withPixelWidth(int width) {
		this.setWidth(width, Unit.PIXELS);
		return this;
	}
}
