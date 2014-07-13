package org.issg.ibis.domain.search;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.TestResourceFactory;
import org.issg.ibis.domain.view.QResourceDescription;
import org.issg.ibis.domain.view.ResourceDescription;
import org.issg.ibis.responsive.IbisUI;
import org.jrc.edit.Dao;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Injector;
import com.mysema.query.SearchResults;
import com.mysema.query.jpa.impl.JPAQuery;

public class QueryDSLTest {

    private Injector injector = TestResourceFactory.getInjector(IbisUI.class);
    private Dao dao;

    @Before
    public void init() {
        dao = injector.getInstance(Dao.class);
    }

    private Species getASingleInvasiveSp() {
        return dao.find(Species.class, 2774l);
    }

    private Species getASingleThreatenedSp() {
        return dao.find(Species.class, 2519l);
    }

    public List<Species> getInvasiveSpp() {

        TypedQuery<Species> q = dao.getEntityManager().createNamedQuery(
                Species.INVASIVE, Species.class);
        return q.getResultList();
    }

    public List<Species> getThreatenedSpp() {

        TypedQuery<Species> q = dao.getEntityManager().createNamedQuery(
                Species.NATIVE, Species.class);
        return q.getResultList();
    }

    @Test
    /**
     * A filter field should take a field and add a filter to a container when the value changes
     * 
     */
    public void testContains() {

        EntityManager em = getEntityManager();
        JPAQuery query = new JPAQuery(em);
        QResourceDescription resourceDescr = QResourceDescription.resourceDescription;

        SearchResults<ResourceDescription> x = query.from(resourceDescr)
                .where(resourceDescr.species.contains(getASingleInvasiveSp()))
                .listResults(resourceDescr);

        for (ResourceDescription t : x.getResults()) {
            System.out.println(t);
        }

    }
    
    
    @Test
    public void testLocation() {
    	
        EntityManager em = getEntityManager();
        TypedQuery<String> q = em.createQuery("select distinct name from LocationView", String.class);
        List<String> l = q.getResultList();
        for (String string : l) {
        	System.out.println(string);
		}
    	
    }

    @Test
    public void testFilter() {

    	//Could be some useful code commented out below
    }

//        EntityManager em = getEntityManager();

//        QDSLFilterController<ResourceDescription> fp = new QDSLFilterController<ResourceDescription>(
//                QResourceDescription.resourceDescription);

//        //Invasive
//        ComboBox cb = new ComboBox();
//        {
//            for (Species sp : getInvasiveSpp()) {
//                cb.addItem(sp);
//            }
//
//            fp.addContainsField(cb,
//                    QResourceDescription.resourceDescription.species);
//            cb.setValue(getASingleInvasiveSp());
//        }
//
//        //Threatened
//        {
//            ComboBox cb2 = new ComboBox();
//            for (Species sp : getThreatenedSpp()) {
//                cb2.addItem(sp);
//            }
//
//            fp.addContainsField(cb2,
//                    QResourceDescription.resourceDescription.species);
//            cb2.setValue(getASingleThreatenedSp());
//        }
//
//        List<ResourceDescription> x = fp.getResults(em);
//
//        for (ResourceDescription resourceDescription : x) {
//            System.out.println(resourceDescription);
//        }
//
//        // The number of locations with that invasive
//        assertEquals(12, x.size());
//
//        
//        
//        cb.setValue(null);
//        
//        x = fp.getResults(em);
//
//        // Un-set the value in a combo
//        
//    }
//
//    private void beginTest() {
//        long startTime = System.nanoTime();
//
//        long endTime = System.nanoTime();
//        long duration = endTime - startTime;
//
//        System.out.println("Duration: " + duration);
//    }
//
//    @Test
//    public void qdslBasic() {
//
//
//    }
//    
//    public Location getSuwarrow(String locName) {
//
//        EntityManager em = getEntityManager();
//        QLocation location = QLocation.location;
//        JPAQuery query = new JPAQuery();
//
//        JPAQuery q1 = query.clone(em);
//        Location suwarrow = q1.from(location).where(location.name.eq(locName))
//                .uniqueResult(location);
//    	
//        return suwarrow;
//    }
//
//    @Test
//    public void impacts() {
//    
//    	Location loc = getSuwarrow("Viti Levu");
//
//        QSpeciesImpact spImpact = QSpeciesImpact.speciesImpact;
//        JPAQuery query = new JPAQuery(dao.getEntityManager());
//        SearchResults<SpeciesImpact> impacts = query.from(spImpact).where(spImpact.location.eq(loc)).listResults(spImpact.speciesImpact);
//        List<SpeciesImpact> resImpacts = impacts.getResults();
//        System.out.println("Size: " + resImpacts.size());
//    }
//    
//    
//    @Test
//    public void resultBySppType() {
//    	
//    	OrganismType ot = dao.find(OrganismType.class, 8l);
////    	System.out.println(ot);
//
//
//        QSpecies sp = QSpecies.species1;
//        JPAQuery query = new JPAQuery(dao.getEntityManager());
//        SearchResults<Species> species = query.from(sp).where(sp.organismType.eq(ot)).listResults(sp.species1);
//
//        List<Species> results = species.getResults();
//        for (Species s : results) {
//        	System.out.println(s);
//		}
////        System.out.println("Size: " + resImpacts.size());
//    }
    

    private EntityManager getEntityManager() {
        return injector.getInstance(EntityManager.class);
    }

}