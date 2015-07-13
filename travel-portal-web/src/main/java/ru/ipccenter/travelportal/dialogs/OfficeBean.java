package ru.ipccenter.travelportal.dialogs;

import org.primefaces.context.RequestContext;
import ru.ipccenter.travelportal.common.model.objects.City;
import ru.ipccenter.travelportal.common.model.objects.Office;
import ru.ipccenter.travelportal.services.CityService;
import ru.ipccenter.travelportal.services.OfficeService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@ViewScoped
@ManagedBean
public class OfficeBean {

    public static final String OFFICE_ID_KEY = "office-id";

    @Inject
    private CityService cityService;
    @Inject
    private OfficeService officeService;

    private List<City> cities;

    private String name;
    private BigInteger cityId;
    private BigInteger officeId;
    private String address;

    public String getId() {
        return (officeId != null) ? officeId.toString(): null;
    }

    public void setId(String officeId) {
        if (officeId != null && !officeId.isEmpty()) {
            this.officeId = new BigInteger(officeId);

            Office office = officeService.getById(this.officeId);
            this.name = office.getName();
            this.address = office.getAddress();
            this.cityId = office.getCityId();
            office.unused();
        } else {
            this.officeId = null;
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getCityId() {
        return cityId;
    }

    public void setCityId(BigInteger cityId) {
        this.cityId = cityId;
    }

    public List<City> getCities() {
        if (cities != null) {
            return cities;
        } else {
            return cities = cityService.getAllCities();
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void save() {
        Office office = (officeId != null)
                ? officeService.getById(officeId)
                : officeService.create();

        office.setName(name.trim());
        office.setCityId(cityId);
        office.setAddress(address.trim());

        RequestContext.getCurrentInstance().closeDialog(null);
    }
}
