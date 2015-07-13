package ru.ipccenter.travelportal.dialogs;

import ru.ipccenter.travelportal.common.model.objects.City;
import ru.ipccenter.travelportal.services.CityService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by Ivan on 19.05.2015.
 */
@RequestScoped
@ManagedBean
public class CityListBean {

    @Inject
    private CityService cityService;

    public List<City> getCities() {
        return cityService.getAllCities();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteCity(BigInteger id) {
        City city = cityService.getById(id);
        city.delete();
    }

    public void openCity(final BigInteger cityId) {

    }
}
