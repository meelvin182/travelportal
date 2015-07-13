package ru.ipccenter.travelportal.ejb.stateless;

import ru.ipccenter.travelportal.common.model.TPObject;
import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.TPObjectNotFoundException;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ObjectType;
import ru.ipccenter.travelportal.common.utils.BeanLookupHelper;
import ru.ipccenter.travelportal.metamodel.Dao;
import ru.ipccenter.travelportal.metamodel.entities.MMObject;
import ru.ipccenter.travelportal.metamodel.entities.MMObjectType;

import javax.ejb.*;
import javax.naming.NamingException;
import java.math.BigInteger;

/**
 * Created by Ivan on 12.03.2015.
 */
@Stateless
@Local(TPObjectFactory.class)
public class TPObjectFactoryBean implements TPObjectFactory {

    @EJB
    private Dao dao;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <O extends TPObject> O createObject(Class<O> clazz) {
        MMObject mmObject = new MMObject();

        ObjectType objectTypeAnnotation = clazz.getAnnotation(ObjectType.class);
        BigInteger objectTypeId = new BigInteger(objectTypeAnnotation.id());
        MMObjectType objectType = dao.getById(objectTypeId, MMObjectType.class);

        mmObject.setObjType(objectType);
        mmObject.setName(clazz.getName());

        dao.save(mmObject);

        return createObject(mmObject.getId(), clazz);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <O extends TPObject> O createObject(BigInteger id, Class<O> clazz) throws TPObjectNotFoundException {
        if (id == null) {
            return null;
        }

        if (dao.getById(id, MMObject.class) == null) {
            throw new TPObjectNotFoundException("MMObject with id #" + id + " doesn't exist");
        } else {
            try {
                O object = BeanLookupHelper.lookup(clazz);
                object.setId(id);
                return object;
            } catch (NamingException e) {
                throw new TPObjectNotFoundException("Couldn't build TPObject", e);
            }
        }
    }
}
