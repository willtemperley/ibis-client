package org.issg.ibis.client;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.issg.ibis.client.js.SpeciesInfo;
import org.issg.ibis.domain.Species;
import org.jrc.persist.Dao;
import org.jrc.ui.SimplePanel;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;

public class MapPerspective extends HorizontalLayout implements View {

    private static final String FIELDWIDTH = "300px";
    private TabSheet tabSheet;
    private Dao dao;

    @Inject
    public MapPerspective(Dao dao) {

        setSizeFull();
        setSpacing(true);

        tabSheet = new TabSheet();
        tabSheet.setSizeFull();

        addComponent(tabSheet);
        
        this.dao = dao;
        
        addSpeciesInfo(Species.INVASIVE);
        addSpeciesInfo(Species.THREATENED);

        tabSheet.addTab(new Label(""), "Island");

        LayerViewer lv = new LayerViewer();
        addComponent(lv);
        lv.setSizeFull();

    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub

    }

    private void addSpeciesInfo(String invasive) {

        CssLayout l = new CssLayout();
        tabSheet.addTab(l, "Species");
        
        EntityManager em = dao.getEntityManager();
        TypedQuery<Species> q = em.createNamedQuery(invasive, Species.class);
        List<Species> res = q.getResultList();

//        NativeSelect combo = new NativeSelect("Invasive");
        ComboBox combo = new ComboBox("Invasive");
        combo.setWidth(FIELDWIDTH);
        l.addComponent(combo);

        for (Species species : res) {
            combo.addItem(species);
        }

    }
}