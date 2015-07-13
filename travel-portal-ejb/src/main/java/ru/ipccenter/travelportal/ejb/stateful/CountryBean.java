package ru.ipccenter.travelportal.ejb.stateful;

import ru.ipccenter.travelportal.common.model.objects.Country;
import ru.ipccenter.travelportal.interceptor.binding.SetOnce;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.math.BigInteger;

/**
 * Created by 111 on 20.03.2015.
 */
@Stateful
@Local(Country.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CountryBean extends AbstractTPObject implements Country {

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
