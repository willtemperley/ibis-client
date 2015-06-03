package org.biopama.ui;


import com.vaadin.ui.HorizontalLayout;

/**
 * Simple twin panel view
 * 
 * @author Will Temperley
 *
 */
public class TwinPanelView extends HorizontalLayout {

    private SimplePanel leftPanel;
    private SimplePanel rightPanel;

    public TwinPanelView() {
    	
    	addStyleName("twinpanel-view");

        setSizeFull();
        setSpacing(true);
        
        leftPanel = new SimplePanel();
        addComponent(leftPanel);
        leftPanel.setSizeFull();
        
        rightPanel = new SimplePanel();
        addComponent(rightPanel);
        rightPanel.setSizeFull();
        
    }
    
    public SimplePanel getLeftPanel() {
        return leftPanel;
    }

    public SimplePanel getRightPanel() {
        return rightPanel;
    }
    
    public void setExpandRatios(float a, float b) {
        setExpandRatio(leftPanel, a);
        setExpandRatio(rightPanel, b);
    }

}
