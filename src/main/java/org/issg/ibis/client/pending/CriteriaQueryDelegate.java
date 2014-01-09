package org.issg.ibis.client.pending;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.issg.ibis.domain.RedlistCategory;
import org.issg.ibis.domain.RedlistCategory_;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.Species_;
import org.jrc.persist.Dao;

import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;

/**
 * Quick attempt with this: 
 * https://vaadin.com/book/vaadin6/-/page/jpacontainer.filtering.criteria-api.html
 */
@Deprecated
public class CriteriaQueryDelegate extends DefaultQueryModifierDelegate {

    private Dao dao;
    private Root<Species> root;

    public CriteriaQueryDelegate(Dao dao) {

        this.dao = dao;
    }

    @Override
    public void filtersWillBeAdded(CriteriaBuilder cb,
            CriteriaQuery<?> query, List<Predicate> predicates) {

        RedlistCategory rlc = dao.findByProxyId(RedlistCategory_.label, "Invasive");

        Predicate predicate = cb.notEqual(root.get(Species_.redlistCategory), rlc);

        predicates.add(predicate);

        super.filtersWillBeAdded(cb, query, predicates);
    }
    
    @Override
    public void queryWillBeBuilt(CriteriaBuilder criteriaBuilder,
            CriteriaQuery<?> query) {
        // TODO Auto-generated method stub
        if (root == null) {
            root = (Root<Species>) query.getRoots().iterator().next();;
        }
        super.queryWillBeBuilt(criteriaBuilder, query);
    }
}
