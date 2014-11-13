package org.issg.ibis.perspective.shared;


import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.lec.EntityTable;

import com.vaadin.server.Scrollable;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;

/**
 * Simple twin panel view
 * 
 * @author Will Temperley
 *
 */
public class TwinPanelPerspective extends HorizontalLayout {

	private LayerViewer map;
	private TabSheet tabSheet;
	protected ExportBar exporter = new ExportBar();
	private Panel leftPanel;
	

    public TwinPanelPerspective() {

		addStyleName("content");
        setSizeFull();
        setSpacing(true);
        
        {
        	leftPanel = new Panel();
        	addComponent(leftPanel);
			leftPanel.addStyleName("display-panel");
			leftPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
			//Todo: make repsonsive
			leftPanel.setWidth("600px");
			leftPanel.setHeight("100%");
        }
        
		{
			VerticalLayout rightLayout = new VerticalLayout();
			addComponent(rightLayout);
			rightLayout.setSizeFull();
			rightLayout.setSpacing(true);

			/*
			 * Map
			 */
			map = new LayerViewer();
			map.setSizeFull();
//			map.getMap().addComponent(mapClusterGroup);

			HorizontalLayout mapLayout = new HorizontalLayout();
			mapLayout.setSpacing(true);
			mapLayout.setSizeFull();
			mapLayout.addComponent(map);

			rightLayout.addComponent(mapLayout);

			/*
			 * Tables in tabsheet
			 */
			tabSheet = new TabSheet();

			HorizontalLayout tableLayout = new HorizontalLayout();
			rightLayout.addComponent(tableLayout);
			tableLayout.addComponent(tabSheet);
			tableLayout.setSizeFull();

			tableLayout.addComponent(exporter);
			exporter.setWidth("40px");
			tableLayout.setExpandRatio(tabSheet, 1);
		
			tabSheet.setSizeFull();
			setExpandRatio(rightLayout, 1);
		}
    }
    
    protected void setLeftPanelContent(Component c) {
    	leftPanel.setContent(c);
    }

    protected void addTable(EntityTable<?> speciesLocationTable, String caption) {
    	tabSheet.addTab(speciesLocationTable, caption);
    }
    
    protected Tab getTab(Component component) {
    	return tabSheet.getTab(component);
    }

	protected LayerViewer getMap() {
		return map;
	}

}
