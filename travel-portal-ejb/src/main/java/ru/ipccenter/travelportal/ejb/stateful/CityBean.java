package ru.ipccenter.travelportal.ejb.stateful;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.objects.City;
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
@Local(City.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CityBean extends AbstractTPObject implements City {
    private static final Logger LOG = Logger.getLogger(CityBean.class);

    @Override
    public BigInteger getCountryId() {
        return cacheProvider.provide().getParameter(getId(), ATTR_ID_COUNTRY).getReference();
    }

    @Override
    public void setCountryId(BigInteger countryId) {
        cacheProvider.provide().getParameter(getId(), ATTR_ID_COUNTRY).setReference(countryId);
    }

    @Override
    public Country getCountry() {
        return objectFactory.createObject(getCountryId(), Country.class);
    }

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