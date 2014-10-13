package org.jrc.edit;

import org.issg.ibis.perspective.shared.TileLayerFactory;
import org.vaadin.addon.leaflet.LLayerGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.util.JTSUtil;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class MultiPolygonField extends CustomField<MultiPolygon> {

	private LLayerGroup llg = new LLayerGroup();
	private LMap map = new LMap();

	private MultiPolygonEditWindow editorWindow;

	public MultiPolygonField() {
		
		map.addComponent(llg);
		map.setWidth("100%");
		map.setHeight("300px");
		map.addBaseLayer(TileLayerFactory.getOSM(), "OSM");
		

		map.addClickListener(new LeafletClickListener() {
			
			@Override
			public void onClick(LeafletClickEvent event) {
				// issue with state fixed by this
				doEdit();

			}
		});

	}
	
	@Override
	protected void setInternalValue(MultiPolygon geom) {

		llg.removeAllComponents();

		if (geom != null) {
            for (int i = 0; i < geom.getNumGeometries(); i++) {
                Polygon polygon = (Polygon) geom.getGeometryN(i);
                LPolygon lPolygon = JTSUtil.toPolygon(polygon);
                llg.addComponent(lPolygon);
                lPolygon.addClickListener(new LeafletClickListener() {
					
					@Override
					public void onClick(LeafletClickEvent event) {
						doEdit();
					}
				});

            }
			map.zoomToExtent(geom);
		}

		super.setInternalValue(geom);
	}

	@Override
	protected Component initContent() {
		return map;
	}

	@Override
	public Class<MultiPolygon> getType() {
		return MultiPolygon.class;
	}

	private void doEdit() {
		editorWindow = new MultiPolygonEditWindow();
		editorWindow.setGeom(getInternalValue());
		UI.getCurrent().addWindow(editorWindow);
	
		editorWindow.addCloseListener(new CloseListener() {
	
			@Override
			public void windowClose(CloseEvent e) {
	
				MultiPolygon mp = editorWindow.getGeom();
				setInternalValue(mp);
			}
		});
	}

}
