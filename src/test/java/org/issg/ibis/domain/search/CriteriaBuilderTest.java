package org.issg.ibis.domain.search;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.issg.ibis.client.pending.CriteriaQueryManager;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.TestResourceFactory;
import org.issg.ibis.domain.view.ResourceDescription;
import org.issg.ibis.domain.view.ResourceDescription_;
import org.jrc.persist.Dao;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Injector;

public class CriteriaBuilderTest {

    private Injector injector = TestResourceFactory.getInjector();
    private Dao dao;

    @Before
    public void init() {
        dao = injector.getInstance(Dao.class);
    }

    public Species getASingleInvasiveSp() {

        TypedQuery<Species> q = dao.getEntityManager().createNamedQuery(
                Species.INVASIVE, Species.class);
        Species testobj = q.getResultList().get(0);
        return testobj;

    }

    @Test
    public void testCB() {

        // FQD
        CriteriaQueryManager<ResourceDescription> queryModifier = new CriteriaQueryManager<ResourceDescription>(dao, 20);
        //FIX ALL THIS
//        queryModifier.addExistsPredicate(ResourceDescription_.species,
//                getASingleInvasiveSp());
        
        long startTime = System.nanoTime();

        List<ResourceDescription> rds = queryModifier.getResourceDescriptions();

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        
        System.out.println("Duration: " + duration);

        for (ResourceDescription resourceDescription : rds) {
            System.out.println(resourceDescription);
        }

    }

}