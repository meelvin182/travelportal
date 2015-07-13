package ru.ipccenter.travelportal.dialogs;

import org.primefaces.context.RequestContext;
import ru.ipccenter.travelportal.common.model.objects.City;
import ru.ipccenter.travelportal.common.model.objects.Country;
import ru.ipccenter.travelportal.services.CityService;
import ru.ipccenter.travelportal.services.CountryService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by Ivan on 19.05.2015.
 */
@ViewScoped
@ManagedBean
public class CityBean {

    public static final String CITY_ID_KEY = "city-id";

    @Inject
    private CityService cityService;
    @Inject
    private CountryService coutryService;

    private List<Country> countries;

    private BigInteger cityId;
    private String name;
    private BigInteger countryId;

    public String getId() {
        return (cityId != null) ? cityId.toString(): null;
    }

    public void setId(String cityId) {
        if (cityId != null && !cityId.isEmpty()) {
            this.cityId = new BigInteger(cityId);

            City city = cityService.getById(this.cityId);
            this.name = city.getName();
            this.countryId = city.getCountryId();
            city.unused();
        } else {
            this.cityId = null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getCountryId() {
        return countryId;
    }

    public void setCountryId(BigInteger countryId) {
        this.countryId = countryId;
    }

    public List<Country> getCountries() {
        if (countries != null) {
            return countries;
        } else {
            return countries = coutryService.getAllCountries();
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void save() {
        City city = (cityId != null)
                ? cityService.getById(cityId)
                : cityService.create();

        city.setName(name.trim());
        city.setCountryId(countryId);

        RequestContext.getCurrentInstance().closeDialog(null);
    }
}
