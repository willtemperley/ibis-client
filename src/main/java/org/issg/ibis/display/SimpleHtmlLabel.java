package org.issg.ibis.display;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

public class SimpleHtmlLabel extends Label {
    
    public SimpleHtmlLabel(String content) {
        super();
        setValue(content);
    }
    
    public SimpleHtmlLabel() {
        setContentMode(ContentMode.HTML);
    }

//    @Override
//    public void setWidth(float width, Unit unit) {
//        // NO-OP
//    }
}
