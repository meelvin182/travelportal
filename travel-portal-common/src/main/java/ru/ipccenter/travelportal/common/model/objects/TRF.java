package ru.ipccenter.travelportal.common.model.objects;

import ru.ipccenter.travelportal.common.model.TPObject;
import ru.ipccenter.travelportal.common.model.attributes.Status;
import ru.ipccenter.travelportal.common.model.bridge.annotations.Attribute;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ObjectType;
import ru.ipccenter.travelportal.common.model.bridge.utils.AttributeExtractor;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by meelvin182 on 05.03.15.
 */
@ObjectType(id = "9223372036854775769",
    name = "trf",
    attributes = {
        @Attribute(id = "9223372036854775770", name = "Status"),
        @Attribute(id = "9223372036854775766", name = "Departure Date"),
        @Attribute(id = "9223372036854775765", name = "Return Date"),
        @Attribute(id = "9223372036854775800", name = "Employee"),
        @Attribute(id = "9223372036854775767", name = "Project Manager"),
        @Attribute(id = "9223372036854775764", name = "Destination City"),
        @Attribute(id = "9223372036854775763", name = "Car Rent"),
        @Attribute(id = "9223372036854775762", name = "Pay Cash"),
        @Attribute(id = "9223372036854775761", name = "Hotel"),
        @Attribute(id = "9223372036854775760", name = "Customer"),
        @Attribute(id = "9223372036854775759", name = "Status History"),
})
public interface TRF extends TPObject, Serializable {
    static final BigInteger STATUS_ATTRIBUTE          = AttributeExtractor
            .extractAttributeId(TRF.class, "Status");
    static final BigInteger DEPATURE_DATE_ATTRIBUTE   = AttributeExtractor
            .extractAttributeId(TRF.class, "Departure Date");
    static final BigInteger RETURN_DATE_ATTRIBUTE     = AttributeExtractor
            .extractAttributeId(TRF.class, "Return Date");
    static final BigInteger EMPLOYEE_ATTRIBUTE        = AttributeExtractor
            .extractAttributeId(TRF.class, "Employee");
    static final BigInteger PROJECT_MANAGER_ATTRIBUTE = AttributeExtractor
            .extractAttributeId(TRF.class, "Project Manager");
    static final BigInteger DEST_CITY_ATTRIBUTE       = AttributeExtractor
            .extractAttributeId(TRF.class, "Destination City");
    static final BigInteger CAR_RENT_ATTRIBUTE        = AttributeExtractor
            .extractAttributeId(TRF.class, "Car Rent");
    static final BigInteger PAY_CASH_ATTRIBUTE        = AttributeExtractor
            .extractAttributeId(TRF.class, "Pay Cash");
    static final BigInteger HOTEL_ATTRIBUTE           = AttributeExtractor
            .extractAttributeId(TRF.class, "Hotel");
    static final BigInteger CUSTOMER_ATTRIBUTE        = AttributeExtractor
            .extractAttributeId(TRF.class, "Customer");
    static final BigInteger STATUS_HISTORY_ATTRIBUTE  = AttributeExtractor
            .extractAttributeId(TRF.class, "Status History");

    Status getStatus();
    BigInteger getStatusId();
    void setStatusId(BigInteger statusId, BigInteger userId);
    BigInteger getLastStatusHistoryId();

    Employee getEmployee();
    BigInteger getEmployeeId();
    void setEmployeeId(BigInteger employeeId);

    Employee getProjectManager();
    BigInteger getProjectManagerId();
    void setProjectManagerId(BigInteger projectManagerId);

    Timestamp getDepartureDate();
    void setDepartureDate(Timestamp departureDate);

    Timestamp getReturnDate();
    void setReturnDate(Timestamp returnDate);

    City getDestCity();
    BigInteger getDestCityId();
    void setDestCityId(BigInteger destCityId);

    boolean getCarRent();
    void setCarRent(boolean carRent);

    boolean getPayCash();
    void setPayCash(boolean payCash);

    String getHotel();
    void setHotel(String hotel);

    Customer getCustomer();
    BigInteger getCustomerId();
    void setCustomerId(BigInteger customerId);

    List<StatusHistoryEntry> getStatusHistory();
}
