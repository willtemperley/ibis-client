package org.biopama.ui;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

public class HtmlLabel extends Label {
    
    private static final String SIMPLE_HTML_LABEL = "simple-html-label";

    public HtmlLabel(String content) {
        super();
        setValue(content);
        setContentMode(ContentMode.HTML);
        addStyleName(SIMPLE_HTML_LABEL);
    }
    
    public HtmlLabel() {
        setContentMode(ContentMode.HTML);
        addStyleName(SIMPLE_HTML_LABEL);
    }

    @Override
    public void setValue(String newStringValue) {
    	if (newStringValue == null) {
    		this.setVisible(false);
    	} else {
    		this.setVisible(true);
    	}
    	super.setValue(newStringValue);
    }
//    @Override
//    public void setWidth(float width, Unit unit) {
//        // NO-OP
//    }
}
