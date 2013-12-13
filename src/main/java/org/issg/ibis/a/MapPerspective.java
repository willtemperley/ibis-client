package org.issg.ibis.a;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.issg.ibis.a.event.SearchSelectEventListener;
import org.issg.ibis.client.LayerViewer;
import org.issg.ibis.domain.FacetedSearch;
import org.issg.ibis.domain.Species;
import org.jrc.persist.Dao;
import org.vaadin.addon.leaflet.jts.FeatureMapLayer;

import com.google.code.vaadin.application.uiscope.UIScoped;
import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vividsolutions.jts.geom.Polygon;

@UIScoped
public class MapPerspective extends HorizontalLayout implements View {

    private static final String FIELDWIDTH = "300px";
    private TabSheet tabSheet;
    private Dao dao;
    private SearchPanel searchPanel;
    
    private SpeciesSummaryView speciesSummaryView;
    
    private SpeciesMapManager smm;
    private LayerViewer lv;

    @Inject
    public MapPerspective(final Dao dao) {

        setSizeFull();
        setSpacing(true);

        smm = new SpeciesMapManager(dao);

        tabSheet = new TabSheet();
        tabSheet.setSizeFull();

        addComponent(tabSheet);
        
        this.dao = dao;
        
        searchPanel = new SearchPanel(dao);
        searchPanel.addSearchListener(new SearchSelectEventListener() {
            @Override
            public void onSelect(FacetedSearch facetedSearch) {
                
                if (facetedSearch.getResourceType().getName().equals("Species")) {
                    //FIXME so fragile!!
                    String s = facetedSearch.getId();
                    

                    String[] arr = s.split("/");
                    String o = arr[1];
                    Long i = Long.valueOf(o);
                    Species sp = dao.find(Species.class, i);
                    speciesSummaryView.setSpecies(sp);
                    tabSheet.setSelectedTab(1);
                    
                    FeatureMapLayer fml = smm.setSpecies(sp);
                    
                    lv.getMap().addComponent(fml);
                    
                    Polygon extent = smm.getSpeciesExtent();
                    
                    lv.zoomTo(extent);

                    Page.getCurrent().setUriFragment("!Map/" + s);
                }
                
            }
        });

        tabSheet.addTab(searchPanel, "Search");

        this.speciesSummaryView = new SpeciesSummaryView();
        tabSheet.addTab(speciesSummaryView, "Species");
//        addSpeciesInfo(Species.INVASIVE, "Invasive");
//        addSpeciesInfo(Species.THREATENED, "Threatened");

        tabSheet.addTab(new Label(""), "Island");
        
        lv = new LayerViewer();
        addComponent(lv);
        lv.setSizeFull();
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub

    }

    private void addSpeciesInfo(String invasive, String caption) {

        CssLayout l = new CssLayout();
        tabSheet.addTab(l, "Species");
        
        EntityManager em = dao.getEntityManager();
        TypedQuery<Species> q = em.createNamedQuery(invasive, Species.class);
        List<Species> res = q.getResultList();

//        NativeSelect combo = new NativeSelect("Invasive");
        ComboBox combo = new ComboBox(caption);
        combo.setWidth(FIELDWIDTH);
        l.addComponent(combo);

        for (Species species : res) {
            combo.addItem(species);
        }

    }
}