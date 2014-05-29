package org.issg.ibis.qdsl.experimental;

import com.mysema.query.types.expr.BooleanExpression;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;

public interface StringFieldInterface extends Property<String>, com.vaadin.data.Property.ValueChangeNotifier, Component {
	
	public BooleanExpression getFilter();


}
