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

	private MultiPolygonEditWindow lew;
	

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
//		for (Component c : polys) {
//			map.removeComponent(c);
//		}
//		polys.clear();
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
//			for (Component c : polys) {
//				map.addComponent(c);
//			}
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
		lew = new MultiPolygonEditWindow();
		lew.setGeom(getInternalValue());
		UI.getCurrent().addWindow(lew);
	
		lew.addCloseListener(new CloseListener() {
	
			@Override
			public void windowClose(CloseEvent e) {
	
				MultiPolygon mp = lew.getGeom();
				setInternalValue(mp);
			}
		});
	}

}
