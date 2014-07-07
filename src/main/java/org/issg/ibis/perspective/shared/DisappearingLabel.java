package org.issg.ibis.perspective.shared;

import com.vaadin.ui.Label;

public class DisappearingLabel extends Label {
	
	public DisappearingLabel withCaption(String caption) {
		this.setCaption(caption);
		return this;
	}
	
	@Override
	public void setValue(String newStringValue) {
		if (newStringValue == null) {
			setVisible(false);
			return;
		}
		if (!isVisible()) {
			setVisible(true);
		}

		super.setValue(newStringValue);
	}

}
