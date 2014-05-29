package org.issg.ibis.qdsl.experimental;

import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.StringPath;
import com.vaadin.ui.NativeSelect;

public class StringFilterField extends NativeSelect {
	
	private StringPath stringPath;

	public StringFilterField(StringPath stringPath) {
		this.stringPath = stringPath;
	}
	

	public BooleanExpression getFilter() {
		String val = (String) getValue();
		if (val != null) {
			return stringPath.eq(val);
		}
		return null;
	}
}
