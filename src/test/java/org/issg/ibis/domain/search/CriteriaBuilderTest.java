package org.issg.ibis.domain.search;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.issg.ibis.client.pending.FilteringCriteriaQueryDelegate;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.TestResourceFactory;
import org.issg.ibis.domain.view.ResourceDescription;
import org.issg.ibis.domain.view.ResourceDescriptionSpecies;
import org.issg.ibis.domain.view.ResourceDescriptionSpecies_;
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
        FilteringCriteriaQueryDelegate<ResourceDescription> queryModifier = new FilteringCriteriaQueryDelegate<ResourceDescription>();
        queryModifier.addExistsPredicate(ResourceDescription_.invasiveSpecies,
                getASingleInvasiveSp());

        // Test query
        EntityManager em = dao.getEntityManager();

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<ResourceDescription> query = criteriaBuilder
                .createQuery(ResourceDescription.class);
        Root<ResourceDescription> person = query
                .from(ResourceDescription.class);

        // Main query
        query.select(person);

        // Pass in a list, just to extract
        List<Predicate> predicates = new ArrayList<Predicate>();

        queryModifier.filtersWillBeAdded(criteriaBuilder, query, predicates);

        // and give the created predicates back to the query (done by framework
        // in non-test code)
        Predicate[] arr = new Predicate[predicates.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = predicates.get(i);
        }

        query.where(arr);

        TypedQuery<ResourceDescription> typedQuery = em.createQuery(query);
        List<ResourceDescription> referencedResourceDescriptions = typedQuery
                .getResultList();

        for (ResourceDescription object : referencedResourceDescriptions) {
            System.out.println(object);
        }

    }

}