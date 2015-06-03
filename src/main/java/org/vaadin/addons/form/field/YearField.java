package org.vaadin.addons.form.field;

import java.util.Calendar;

import com.vaadin.ui.ComboBox;

public class YearField extends ComboBox {

    private int year = Calendar.getInstance().get(Calendar.YEAR);
    
    public YearField() {
        
        Integer thisYear = (int) year;
        for (int i = 0; i < 50; i++) {
            addItem(thisYear--);
        }
        
    }
    
    @Override
    public Class<?> getType() {
        return Integer.class;
    }

}
