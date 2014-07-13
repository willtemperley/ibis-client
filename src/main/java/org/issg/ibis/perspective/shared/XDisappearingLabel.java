package org.issg.ibis.perspective.shared;

import com.vaadin.ui.Label;

public class XDisappearingLabel extends Label {
	
	public XDisappearingLabel withCaption(String caption) {
		this.setCaption(caption);
		return this;
	}
	
	@Override
	public void setValue(String newStringValue) {
		if (newStringValue == null || newStringValue.isEmpty()) {
			setVisible(false);
			return;
		}
		if (!isVisible()) {
			setVisible(true);
		}

		super.setValue(newStringValue);
	}

}
