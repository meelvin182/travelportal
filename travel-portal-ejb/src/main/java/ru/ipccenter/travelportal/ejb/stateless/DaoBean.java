package ru.ipccenter.travelportal.ejb.stateless;


import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.metamodel.Dao;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;

@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless
@Local(Dao.class)
public class DaoBean implements Dao {

    @PersistenceContext(unitName = "ru.ipccenter.travelportal.pu", type = PersistenceContextType.TRANSACTION)
    private EntityManager em;

    @EJB
    JdbcTemplate jdbcTemplate;

    public DaoBean() {}

    public <T> void delete(T entity) {
        em.remove(em.merge(entity));
    }

    public <T> void delete(Collection<T> entities){
        for(T entity: entities)
            delete(entity);
    }

    public <T> void save(T entity) {
        em.persist(entity);
    }

    public <T> void save(Collection<T> entities) {
        for(T entity: entities)
            save(entity);
    }

    public <T> void refresh(T entity) {
        em.refresh(entity);
    }

    public <T> void refresh(Collection<T> entities) {
        for(T entity: entities)
            refresh(entity);
    }

    public <T> T getById(Object id, Class<T> entityClass) {
        return em.find(entityClass, id);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(Class<T> entityClass) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery(entityClass);
        Root<T> entity = cq.from(entityClass);
        cq.select(entity);
        return em.createQuery(cq).getResultList();
    }


    /**
     * @param sql the string contained JPQL-query
     * @param clazz Entity's class
     * @param <T> Entity
     * @return All columns from one table
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getBySql(String sql, Class<T> clazz) {
        return em.createNativeQuery(sql, clazz).getResultList();
    }

}
