package ru.ipccenter.travelportal.dialogs;

import org.primefaces.context.RequestContext;
import ru.ipccenter.travelportal.common.model.objects.Country;
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
public class CountryBean {

    public static final String COUNTRY_ID_KEY = "country-id";

    @Inject
    private CountryService countryService;

    private List<Country> countries;

    private BigInteger countryId;
    private String name;

    public String getId() {
        return (countryId != null) ? countryId.toString(): null;
    }

    public void setId(String countryId) {
        if (countryId != null && !countryId.isEmpty()) {
            this.countryId = new BigInteger(countryId);

            Country country = countryService.getById(this.countryId);
            this.name = country.getName();
            country.unused();
        } else {
            this.countryId = null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void save() {
        Country country = (countryId != null)
                ? countryService.getById(countryId)
                : countryService.create();

        country.setName(name.trim());
        RequestContext.getCurrentInstance().closeDialog(null);
    }
}
