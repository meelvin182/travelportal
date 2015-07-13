package ru.ipccenter.travelportal.dialogs;

import ru.ipccenter.travelportal.common.model.objects.Country;
import ru.ipccenter.travelportal.services.CountryService;

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
public class CountryListBean {

    @Inject
    private CountryService countryService;

    public List<Country> getCountries() {
        return countryService.getAllCountries();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteCountry(BigInteger id) {
        Country city = countryService.getById(id);
        city.delete();
    }

    public void openCountry(final BigInteger countryId) {

    }
}
