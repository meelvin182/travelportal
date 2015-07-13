package ru.ipccenter.travelportal.ejb.stateful;

import ru.ipccenter.travelportal.common.model.objects.Customer;
import ru.ipccenter.travelportal.interceptor.binding.SetOnce;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.math.BigInteger;

/**
 * Created by meelvin182 on 20.03.15.
 */
@Stateful
@Local
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CustomerBean extends AbstractTPObject implements Customer {

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public BigInteger getId() {
        return super.getId();
    }

    @Override
    @SetOnce
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void setId(BigInteger id) {
        super.setId(id);
    }
}
