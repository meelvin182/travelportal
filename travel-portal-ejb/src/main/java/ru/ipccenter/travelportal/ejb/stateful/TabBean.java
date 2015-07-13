package ru.ipccenter.travelportal.ejb.stateful;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.TPListAttributeFactory;
import ru.ipccenter.travelportal.common.model.attributes.Status;
import ru.ipccenter.travelportal.common.model.attributes.Tab;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ListValueType;
import ru.ipccenter.travelportal.interceptor.binding.SetOnce;
import ru.ipccenter.travelportal.metamodel.Dao;
import ru.ipccenter.travelportal.metamodel.entities.MMListValue;

import javax.ejb.*;
import java.math.BigInteger;
import java.util.List;

@Stateful
@Local(Tab.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TabBean implements Tab {
    private static final Logger LOG = Logger.getLogger(TabBean.class);

    @EJB
    private TPListAttributeFactory listAttributeFactory;
    @EJB
    private Dao dao;

    private BigInteger id;

    @Override
    @SetOnce
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public BigInteger getId() {
        return this.id;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void setId(BigInteger id) {
        this.id = id;
    }

    @Override
    public String getValue() {
        return dao.getById(id, MMListValue.class).getValue();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public BigInteger getListValueTypeId() {
        return new BigInteger(Tab.class.getAnnotation(ListValueType.class).id());
    }

    @Override
    public List<Status> getAllValues() {
        return listAttributeFactory.createAllListAttributes(Status.class);
    }

    @Override
    public String toString() {
        return getValue();
    }
}
