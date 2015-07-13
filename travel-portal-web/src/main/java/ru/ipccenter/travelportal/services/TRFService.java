package ru.ipccenter.travelportal.services;


import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.attributes.Status;
import ru.ipccenter.travelportal.common.model.objects.City;
import ru.ipccenter.travelportal.common.model.objects.Employee;
import ru.ipccenter.travelportal.common.model.objects.Office;
import ru.ipccenter.travelportal.common.model.objects.TRF;
import ru.ipccenter.travelportal.data.holders.EmployeeInfo;
import ru.ipccenter.travelportal.data.holders.Entry;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@ApplicationScoped
public class TRFService {

    @EJB
    TPObjectFactory objectFactory;

    @Transactional(Transactional.TxType.REQUIRED)
    public Entry createEntry(BigInteger trfId) {
        Entry entry = new Entry();
        TRF trf = objectFactory.createObject(trfId, TRF.class);
        entry.setTrfId(trf.getId().toString());

        Employee employee = trf.getEmployee();
        if (employee != null) {
            EmployeeInfo employeeInfo = new EmployeeInfo(employee);
            entry.setEmployeeInfo(employeeInfo);
        }


        Timestamp departureDate = trf.getDepartureDate();
        if(departureDate != null)
            entry.setDepartureDate(new SimpleDateFormat("MM/dd/yyyy").format(departureDate));

        Timestamp returnDate = trf.getReturnDate();
        if(returnDate != null)
            entry.setReturnDate(new SimpleDateFormat("MM/dd/yyyy").format(returnDate));

        City city = trf.getDestCity();
        if(city != null)
            entry.setDestCity(city.getName());

        Status status = trf.getStatus();
        if(status != null)
            entry.setStatus(status.getValue());

        entry.setName(trf.getName());

        return entry;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Entry createEntryWithOffice(BigInteger trfId) {
        Entry entry = createEntry(trfId);
        TRF trf = objectFactory.createObject(trfId, TRF.class);
        Employee employee = trf.getEmployee();
        if(employee != null) {
            Office office = employee.getOffice();
            if(office != null)
                entry.setOffice(office.getName());
            else
                entry.setOffice("NO");
        }
        else
            entry.setOffice("NO");
        return entry;
    }
}
