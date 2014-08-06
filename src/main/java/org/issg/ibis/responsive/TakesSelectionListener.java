package org.issg.ibis.responsive;

import org.vaadin.maddon.fields.MValueChangeListener;

public interface TakesSelectionListener<T> {

	public abstract void addMValueChangeListener(
			MValueChangeListener<T> listener);

}