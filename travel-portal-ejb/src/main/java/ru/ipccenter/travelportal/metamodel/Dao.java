package ru.ipccenter.travelportal.metamodel;


import javax.ejb.Local;
import java.util.Collection;
import java.util.List;

@Local
public interface Dao {

    public <T> void delete(T entity);
    public <T> void delete(Collection<T> entities);
    public <T> void save(T entity);
    public <T> void save(Collection<T> entities);
    public <T> void refresh(T entity);
    public <T> void refresh(Collection<T> entities);
    public <T> T getById(Object id, Class<T> entityclass);
    public <T> List<T> findAll(Class<T> entityClass);
    public <T> List<T> getBySql(String sql, Class<T> clazz);
}
