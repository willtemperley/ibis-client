package org.issg.ibis.qdsl.experimental;

import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.StringPath;
import com.vaadin.ui.TextField;

public class StringContainsField extends TextField implements StringFieldInterface {

	private StringPath path;

	public StringContainsField(StringPath path) {
		this.path = path;
		setImmediate(true);
		setNullRepresentation("");
	}

	@Override
	public BooleanExpression getFilter() {
		if (getValue() == null || getValue().equals("")) {
			return null;
		}
		return path.containsIgnoreCase(getValue());
	}

}
