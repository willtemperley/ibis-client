package org.biopama.ibis.perspective.shared;

import org.vaadin.maddon.fields.MValueChangeListener;

public interface TakesSelectionListener<T> {

	public abstract void addMValueChangeListener(
			MValueChangeListener<T> listener);

}