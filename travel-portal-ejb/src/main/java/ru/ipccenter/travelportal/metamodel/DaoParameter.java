package ru.ipccenter.travelportal.metamodel;

import ru.ipccenter.travelportal.metamodel.entities.MMParameter;

import javax.ejb.Local;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * Created by Anastasia on 12.03.2015.
 */
@Local
public interface DaoParameter {
    /**
     * Delete parameter from database
     * @param parameter - object from MMParameter table, which you want to delete
     */
    public void delete (MMParameter parameter) throws SQLException;

    /**
     * Delete collection of parameters from database
     * @param parameters - collections of objects from MMParameter table, which you want to delete
     */
    public void delete(Collection<MMParameter> parameters) throws SQLException;

    /**
     * Save the parameter to database
     * @param parameter - object from MMParameter table, which you want to save
     */
    public void save(MMParameter parameter) throws SQLException;

    /**
     * Save collection of parameters to database
     * @param parameters - collection of objects from MMParameter table, which you want to save
     */
    public void save(Collection<MMParameter> parameters) throws SQLException;

    /**
     * Refresh object from database: despite of changes without saving it gets object from database again and saves this
     * one
     * @param parameter - object from MMParameter table, which you want to refresh
     */
    public void refresh(MMParameter parameter) throws SQLException;

    /**
     * Refresh collection of objects from database: despite of changes without saving it gets objects from database
     * again and saves them
     * @param parameters - collection of objects from MMParameter table, which you want to refresh
     */
    public void refresh(Collection<MMParameter> parameters) throws SQLException;

    /**
     * Gets all parameters with such attribute ID and object ID
     * @param objId - ObjectID from MMObject table
     * @param attrId - AttributeID from MMAttribute table
     * @return list of MMParameter's objects
     */
    public List<MMParameter> getByObjIdAttrId (BigInteger objId, BigInteger attrId) throws SQLException;

    /**
     * Gets parameter with such attribute ID and object ID. Such parameter should be unique. If you want to get more
     * than one object, use getByObjIdAttrId.
     * @param objId - ObjectID from MMObject table
     * @param attrId - AttributeID from MMAttribute table
     * @return MMParameter object
     */
    public MMParameter getByObjIdAttrIdSingle (BigInteger objId, BigInteger attrId) throws SQLException;
}