package ru.ipccenter.travelportal;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.objects.City;
import ru.ipccenter.travelportal.common.model.objects.Customer;
import ru.ipccenter.travelportal.common.model.objects.Employee;
import ru.ipccenter.travelportal.common.model.objects.TRF;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ObjectType;
import ru.ipccenter.travelportal.data.holders.CityInfo;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
* Created by user on 05.04.2015.
*/
@ManagedBean(name = "trfViewBean")
@ViewScoped
public class NewTRFViewBean {
    private static final BigInteger EMPLOYEE_TYPE_ID = new BigInteger(
            Employee.class.getAnnotation(ObjectType.class).id());
    private static final BigInteger CUSTOMER_TYPE_ID = new BigInteger(
            Customer.class.getAnnotation(ObjectType.class).id());
    private static final BigInteger CITY_TYPE_ID = new BigInteger(
            City.class.getAnnotation(ObjectType.class).id());
    private static final Logger LOG = Logger.getLogger(NewTRFViewBean.class);

    public static final String TRF_ID_REQUEST_PARAM = "trfId";
    public static final String NEW_TRF_KEY = "NEW_TRF";

    @EJB
    private TPObjectFactory objectFactory;
    @EJB
    private JdbcTemplate jdbcTemplate;

    private BigInteger trfId;
    private BigInteger statusId;
    private BigInteger employee;
    private BigInteger projectManager;
    private Timestamp departureDate;
    private Timestamp returnDate;
    private BigInteger destCityId;



    public void createTrf() {
//        trf = objectFactory.createObject(TRF.class);
    }



    public List<Employee> getEmployees() {
        return null;
    }

    public List<Customer> getCustomers() {
        return null;
    }

    public List<CityInfo> getCities() {

        try {
            return jdbcTemplate.executeSelect(
                    "select object_id, name from tp_objects where object_type_id = ?",
                    new Object[][]{
                            {Types.NUMERIC, CITY_TYPE_ID}
                    },
                    new ResultSetHandler<List<CityInfo>>() {
                        @Override
                        public List<CityInfo> handle(ResultSet resultSet) throws SQLException {
                            List<CityInfo> list = new LinkedList<>();

                            while (resultSet.next()) {
                                list.add(new CityInfo(
                                        resultSet.getBigDecimal(1).toBigInteger(),
                                        resultSet.getString(2)));
                            }
                            return list;
                        }
                    }
            );
        } catch (SQLException e) {
            LOG.error(e);
            return Collections.emptyList();
        }
    }

    public void loadTrfInfo(ActionEvent ae) {
        Map<String, String> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String trfID = requestParams.get(TRF_ID_REQUEST_PARAM);

        if (trfID != null && NEW_TRF_KEY.equals(trfID)) {
            // skip
        } else if (trfID != null && !trfID.isEmpty()) {
            setTrfId(new BigInteger(trfID));
            TRF trf = objectFactory.createObject(trfId, TRF.class);
            setEmployee(trf.getEmployeeId());
        } else {
            throw new RuntimeException("");
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void submitListener(ActionEvent ae) {
        TRF trf = null;

        if (trfId != null) {
            trf = objectFactory.createObject(trfId, TRF.class);
        } else {
            trf = objectFactory.createObject(TRF.class);
        }

        trf.setEmployeeId(employee);
        // ...
    }

//    private void initEmployees() {
//        try {
//            employees = jdbcTemplate.executeSelect(
//                    "select object_id from tp_objects where object_type_id = ?",
//                    new Object[][]{
//                            {Types.NUMERIC, EMPLOYEE_TYPE_ID}
//                    },
//                    new ResultSetHandler<List<Employee>>() {
//                        @Override
//                        public List<Employee> handle(ResultSet resultSet) throws SQLException {
//                            List<Employee> list = new LinkedList<>();
//
//                            while (resultSet.next()) {
//                                BigInteger employee_id = new BigInteger(resultSet.getNString("object_id"));
//                                list.add(objectFactory.createObject(employee_id, Employee.class));
//                            }
//                            return list;
//                        }
//                    }
//            );
//        } catch (SQLException e) {
//            LOG.error(e);
//            throw new DataAccessException("Can't get employees", e);
//        }
//    }
//
//    private void initCustomers() {
//        try {
//            customers = jdbcTemplate.executeSelect(
//                    "select object_id from tp_objects where object_type_id = ?",
//                    new Object[][]{
//                            {Types.NUMERIC, CUSTOMER_TYPE_ID}
//                    },
//                    new ResultSetHandler<List<Customer>>() {
//                        @Override
//                        public List<Customer> handle(ResultSet resultSet) throws SQLException {
//                            List<Customer> list = new LinkedList<>();
//
//                            while (resultSet.next()) {
//                                BigInteger customer_id = new BigInteger(resultSet.getNString("object_id"));
//                                list.add(objectFactory.createObject(customer_id, Customer.class));
//                            }
//                            return list;
//                        }
//                    }
//            );
//        } catch (SQLException e) {
//            LOG.error(e);
//            throw new DataAccessException("Can't get countries", e);
//        }
//    }

    public BigInteger getTrfId() {
        return trfId;
    }

    public void setTrfId(BigInteger trfId) {
        this.trfId = trfId;
    }

    public BigInteger getStatusId() {
        return statusId;
    }

    public void setStatusId(BigInteger statusId) {
        this.statusId = statusId;
    }

    public BigInteger getEmployee() {
        return employee;
    }

    public void setEmployee(BigInteger employee) {
        this.employee = employee;
    }

    public BigInteger getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(BigInteger projectManager) {
        this.projectManager = projectManager;
    }

    public Timestamp getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Timestamp departureDate) {
        this.departureDate = departureDate;
    }

    public Timestamp getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Timestamp returnDate) {
        this.returnDate = returnDate;
    }

    public BigInteger getDestCityId() {
        return destCityId;
    }

    public void setDestCityId(BigInteger destCityId) {
        this.destCityId = destCityId;
    }


}
