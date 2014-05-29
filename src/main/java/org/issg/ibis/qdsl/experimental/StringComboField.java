package org.issg.ibis.qdsl.experimental;

import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.StringPath;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.NativeSelect;

public class StringComboField extends CustomField<String> implements StringFieldInterface {
	
	private StringPath stringPath;
	NativeSelect ns = new NativeSelect();

	public StringComboField(StringPath stringPath) {
		this.stringPath = stringPath;
	}
	
	
	@Override
	public void setWidth(String width) {
		ns.setWidth(width);
		super.setWidth(width);
	}

	public BooleanExpression getFilter() {
		String val = (String) ns.getValue();
		if (val != null) {
			return stringPath.eq(val);
		}
		return null;
	}


	@Override
	protected Component initContent() {
		return ns;
	}
	
	@Override
	public void setValue(String newFieldValue)
			throws com.vaadin.data.Property.ReadOnlyException,
			ConversionException {
		ns.setValue(newFieldValue);
	}

	@Override
	public void addValueChangeListener(
			com.vaadin.data.Property.ValueChangeListener listener) {
		ns.addValueChangeListener(listener);
	}

	@Override
	public Class<? extends String> getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addItem(String item) {
		ns.addItem(item);
	}
}
