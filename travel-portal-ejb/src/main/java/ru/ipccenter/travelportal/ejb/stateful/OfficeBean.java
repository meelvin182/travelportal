package ru.ipccenter.travelportal.ejb.stateful;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.objects.City;
import ru.ipccenter.travelportal.common.model.objects.Office;
import ru.ipccenter.travelportal.interceptor.binding.SetOnce;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.math.BigInteger;

/**
 * Created by meelvin182 on 19.03.15.
 */

@Stateful
@Local(Office.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class OfficeBean extends AbstractTPObject implements Office  {
    private static final Logger LOG = Logger.getLogger(OfficeBean.class);

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

    @Override
    public BigInteger getCityId()  {
        return cacheProvider.provide().getParameter(getId(), CITY_ATTRIBUTE).getReference();
    }

    @Override
    public void setCityId(BigInteger cityId) {
        cacheProvider.provide().getParameter(getId(), CITY_ATTRIBUTE).setReference(cityId);
    }

    @Override
    public String getAddress(){
        return cacheProvider.provide().getParameter(getId(),ADDRESS_ATTRIBUTE).getValue();
    }

    @Override
    public void setAddress(String address)  {
        cacheProvider.provide().getParameter(getId(), ADDRESS_ATTRIBUTE).setValue(address);
    }

    @Override
    public City getCity()  {
          return objectFactory.createObject(getCityId(), City.class);
    }

    public String toString() {
        return Office.class.getName() + "#" + getId().toString();
    }
}
