package ru.ipccenter.travelportal.dialogs;

import ru.ipccenter.travelportal.common.model.objects.Office;
import ru.ipccenter.travelportal.services.OfficeService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@RequestScoped
@ManagedBean
public class OfficeListBean {

    @Inject
    private OfficeService officeService;

    public List<Office> getOffices() {
        return officeService.getOffices();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteOffice(BigInteger id) {
        Office office = officeService.getById(id);
        office.delete();
    }
}