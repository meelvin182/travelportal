package ru.ipccenter.travelportal.ejb.stateful;


import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.attributes.Status;
import ru.ipccenter.travelportal.common.model.objects.*;
import ru.ipccenter.travelportal.interceptor.binding.SetOnce;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.metamodel.entities.MMParameter;

import javax.ejb.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateful
@Local(TRF.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TRFBean extends AbstractTPObject implements TRF {
    private static final Logger LOG = Logger.getLogger(TRFBean.class);

    @EJB
    private JdbcTemplate jdbcTemplate;

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
    public void setStatusId(BigInteger statusId, BigInteger userId) {
        //last status of StatusHistoryEntry
        StatusHistoryEntry historyEntry = objectFactory.createObject(StatusHistoryEntry.class);

        historyEntry.setStatusId(statusId);
        historyEntry.setUserId(userId);
        historyEntry.setChangeTime(new Timestamp(new Date().getTime()));

        List<MMParameter> refs = cacheProvider.provide().getParametersWithEmptyLast(getId(), STATUS_HISTORY_ATTRIBUTE);
        MMParameter ref2statusEntry = refs.get(refs.size() - 1);
        ref2statusEntry.setReference(historyEntry.getId());
    }

    @Override
    public BigInteger getLastStatusHistoryId() {
        //last status of StatusHistoryEntry
        List<MMParameter> refs = cacheProvider.provide().getParameters(getId(), STATUS_HISTORY_ATTRIBUTE);
        return (refs.size() != 0)
                ? refs.get(refs.size() - 1).getReference()
                : null;
    }

    @Override
    public BigInteger getStatusId() {
        BigInteger statusHistoryId = getLastStatusHistoryId();
        return cacheProvider.provide().getParameter(statusHistoryId, STATUS_ATTRIBUTE).getListValueId();
    }


    @Override
    public BigInteger getEmployeeId() {
        return cacheProvider.provide().getParameter(getId(), EMPLOYEE_ATTRIBUTE).getReference();
    }

    @Override
    public void setEmployeeId(BigInteger EmployeeId) {
        cacheProvider.provide().getParameter(getId(), EMPLOYEE_ATTRIBUTE).setReference(EmployeeId);
    }

    @Override
    public Employee getProjectManager() {
        return objectFactory.createObject(getProjectManagerId(), Employee.class);
    }

    @Override
    public BigInteger getProjectManagerId() {
        return cacheProvider.provide().getParameter(getId(), PROJECT_MANAGER_ATTRIBUTE).getReference();
    }

    @Override
    public void setProjectManagerId(BigInteger ProjectManagerId) {
        cacheProvider.provide().getParameter(getId(), PROJECT_MANAGER_ATTRIBUTE).setReference(ProjectManagerId);
    }

    @Override
    public Timestamp getDepartureDate() {
        return cacheProvider.provide().getParameter(getId(), DEPATURE_DATE_ATTRIBUTE).getDateValue();
    }

    @Override
    public void setDepartureDate(Timestamp DepartureDate) {
        cacheProvider.provide().getParameter(getId(), DEPATURE_DATE_ATTRIBUTE).setDateValue(DepartureDate);
    }

    @Override
    public Timestamp getReturnDate() {
        return cacheProvider.provide().getParameter(getId(), RETURN_DATE_ATTRIBUTE).getDateValue();
    }

    @Override
    public void setReturnDate(Timestamp ReturnDate) {
        cacheProvider.provide().getParameter(getId(), RETURN_DATE_ATTRIBUTE).setDateValue(ReturnDate);
    }

    public City getDestCity() {
        return objectFactory.createObject(getDestCityId(), City.class);
    }

    @Override
    public BigInteger getDestCityId() {
        return cacheProvider.provide().getParameter(getId(), DEST_CITY_ATTRIBUTE).getReference();
    }

    @Override
    public void setDestCityId(BigInteger DestCityId) {
        cacheProvider.provide().getParameter(getId(), DEST_CITY_ATTRIBUTE).setReference(DestCityId);
    }

    @Override
    public boolean getCarRent() {
        return new Boolean(cacheProvider.provide().getParameter(getId(), CAR_RENT_ATTRIBUTE).getValue());
    }

    @Override
    public void setCarRent(boolean CarRent) {
        cacheProvider.provide().getParameter(getId(), CAR_RENT_ATTRIBUTE).setValue(new Boolean(CarRent).toString());
    }

    @Override
    public boolean getPayCash() {
        return new Boolean(cacheProvider.provide().getParameter(getId(), PAY_CASH_ATTRIBUTE).getValue());
    }

    @Override
    public void setPayCash(boolean PayCash) {
        cacheProvider.provide().getParameter(getId(), PAY_CASH_ATTRIBUTE).setValue(new Boolean(PayCash).toString());
    }

    @Override
    public String getHotel() {
        return cacheProvider.provide().getParameter(getId(), HOTEL_ATTRIBUTE).getValue();
    }

    @Override
    public void setHotel(String Hotel) {
        cacheProvider.provide().getParameter(getId(), HOTEL_ATTRIBUTE).setValue(Hotel);
    }

    @Override
    public BigInteger getCustomerId() {
        return cacheProvider.provide().getParameter(getId(), CUSTOMER_ATTRIBUTE).getReference();
    }

    @Override
    public void setCustomerId(BigInteger CustomerId) {
        cacheProvider.provide().getParameter(getId(), CUSTOMER_ATTRIBUTE).setReference(CustomerId);
    }

    @Override
    public Employee getEmployee() {
        return objectFactory.createObject(getEmployeeId(), Employee.class);
    }

    @Override
    public Customer getCustomer() {
        return objectFactory.createObject(getCustomerId(), Customer.class);
    }

    @Override
    public Status getStatus() {
        BigInteger statusId = getStatusId();
        return (statusId != null)
            ? listAttributeFactory.createListAttribute(statusId, Status.class)
            : null;
    }

    @Override
    public List<StatusHistoryEntry> getStatusHistory() {
        List<MMParameter> statusHistoryEntryRefs = cacheProvider.provide().getParameters(getId(), STATUS_HISTORY_ATTRIBUTE);

        List<StatusHistoryEntry> entries = new ArrayList<>();
        for(MMParameter ref: statusHistoryEntryRefs) {
            entries.add(objectFactory.createObject(ref.getReference(), StatusHistoryEntry.class));
        }

        return entries;
    }
}
