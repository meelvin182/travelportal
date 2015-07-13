package ru.ipccenter.travelportal.dialogs;


import org.primefaces.context.RequestContext;
import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ObjectType;
import ru.ipccenter.travelportal.common.model.objects.Employee;
import ru.ipccenter.travelportal.common.model.objects.StatusHistoryEntry;
import ru.ipccenter.travelportal.common.model.objects.TRF;
import ru.ipccenter.travelportal.common.model.objects.User;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;
import ru.ipccenter.travelportal.session.CurrentUserBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.*;

@ViewScoped
@ManagedBean(name = "TRFManager")
public class TRFManager {
    public static final String GET_ALL_CITIES_QUERY = "select name from tp_objects where object_type_id = 9223372036854775780";
    public static final String GET_ALL_CUSTOMERS_QUERY = "select name from tp_objects where object_type_id = 9223372036854775757";
    public static final String GET_CITY_ID_QUERY = "select object_id from tp_objects where object_type_id = 9223372036854775780 and name = ?";
    public static final String GET_CUSTOMER_ID_QUERY = "select object_id from tp_objects where object_type_id = 9223372036854775757 and name = ?";

    public static final String GET_EMPLOYEES_FOR_CURRENT_USER =
            "select object_id, name\n" +
            "from tp_objects\n" +
            "where object_id in\n" +
            "(" +
            "select object_id\n" +
            "from tp_params\n" +
            "where reference = ?\n" + /* current emmployee */
            ")" +
            "and object_type_id = ?"; /* employee object_type_id */

    public static final BigInteger EMPLOYEE_OBJECT_TYPE_ID = new BigInteger(
            Employee.class.getAnnotation(ObjectType.class).id());
    public static final String ENTERING_STATUS_ID = "9223372036854775776";
    public static final String READY_STATUS_ID = "9223372036854775775";
    public static final String CANCELLED_STATUS_ID = "9223372036854775772";
    public static final String REJECTED_STATUS_ID = "9223372036854775773";
    public static final String COMPLETED_STATUS_ID = "9223372036854775774";

    //TODO  make type extractor
    @EJB
    private TPObjectFactory objectFactory;

    @EJB
    JdbcTemplate jdbcTemplate;

    @Inject
    CurrentUserBean currentUserBean;


    private TRF trf;
    private Employee employee;
    private String employeeName;



    private BigInteger trfId;
    private BigInteger statusId;
    private BigInteger projectManager;
    private Date departureDate;
    private Date returnDate;
    private String destCity;
    private String customer;
    private boolean carRent;
    private boolean payCash = true;
    private String hotel;
    private User user;

    List<String> cities;
    List<String> customers;
    Map<String, BigInteger> employees;

    List<StatusHistoryEntry> statusHistoryEntries;

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

    public BigInteger getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(BigInteger projectManager) {
        this.projectManager = projectManager;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public boolean getPayCash() {
        return payCash;
    }

    public void setPayCash(boolean payCash) {
        this.payCash = payCash;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getDestCity() {
        return destCity;
    }

    public void setDestCity(String destCity) {
        this.destCity = destCity;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public boolean getCarRent() {
        return carRent;
    }

    public void setCarRent(boolean carRent) {
        this.carRent = carRent;
    }

    public List<StatusHistoryEntry> getStatusHistoryEntries() {
        return statusHistoryEntries;
    }


    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest)context.getRequest();
        HttpSession session = request.getSession();
        Object attribute = session.getAttribute("trfId");
        if (attribute == null)
            trfId = null;
        else
            trfId = new BigInteger((String)attribute);

        if(trfId != null) {
            trf = objectFactory.createObject(trfId, TRF.class);
            departureDate = trf.getDepartureDate();
            returnDate = trf.getReturnDate();
            payCash = trf.getPayCash();
            carRent = trf.getCarRent();
            hotel = trf.getHotel();
            if(trf.getDestCity() == null)
                destCity = null;
            else
                destCity = trf.getDestCity().getName();
            if(trf.getCustomer() == null)
                customer = null;
            else
                customer = trf.getCustomer().getName();
            statusHistoryEntries = trf.getStatusHistory();
        }

    }


    public void submit() {
        if(departureDate == null) {
            closeDialog("Please, select departure date");
            return;
        }

        if (returnDate == null) {
            closeDialog("Please, select return date");
            return;
        }

        user = currentUserBean.getUser();

        if(trfId == null)
            createTrf();
        else
            loadTrf(trfId);

        setParameters();

        closeDialog("Saved");
    }

    public void cancel() {
        if(departureDate == null) {
            closeDialog("Please, select departure date");
            return;
        }

        if (returnDate == null) {
            closeDialog("Please, select return date");
            return;
        }
        user = currentUserBean.getUser();
        cancelTrf(trfId);
        setParameters();
        closeDialog("Saved");
    }

    public void reject() {
        if(departureDate == null) {
            closeDialog("Please, select departure date");
            return;
        }

        if (returnDate == null) {
            closeDialog("Please, select return date");
            return;
        }
        user = currentUserBean.getUser();
        rejectTrf(trfId);
        setParameters();
        closeDialog("Saved");
    }
    private void cancelTrf(BigInteger trfId) {
        trf = objectFactory.createObject(trfId, TRF.class);
        trf.setStatusId(new BigInteger(CANCELLED_STATUS_ID), user.getId());
    }

    private void rejectTrf(BigInteger trfId) {
        trf = objectFactory.createObject(trfId, TRF.class);
        trf.setStatusId(new BigInteger(REJECTED_STATUS_ID), user.getId());
    }

    private void updateCities() {
        try {
            jdbcTemplate.executeSelect(GET_ALL_CITIES_QUERY, new ResultSetHandler<List<String>>() {
                @Override
                public List<String> handle(ResultSet resultSet) throws SQLException {
                    cities = new LinkedList<>();
                    while(resultSet.next()) {
                        cities.add(resultSet.getString(1));
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            cities = new LinkedList<>();
        }
    }
    public List<String> getAllCities() {
        if(cities == null)
            updateCities();
        return cities;
    }

    public void setCity(String city) {
        try {
            List<BigInteger> list = jdbcTemplate.executeSelect(GET_CITY_ID_QUERY,
                    new Object[][] {{ Types.VARCHAR, city }}, new ResultSetHandler<List<BigInteger>>() {
                @Override
                public List<BigInteger> handle(ResultSet resultSet) throws SQLException {
                    resultSet.next();
                    List list = new ArrayList<BigInteger>();
                    list.add(resultSet.getBigDecimal(1).toBigInteger());
                    return list;
                }
            });
            trf.setDestCityId(list.get(0));
        } catch (SQLException e) {}
    }

    private void updateCustomers() {
        try {
            jdbcTemplate.executeSelect(GET_ALL_CUSTOMERS_QUERY, new ResultSetHandler<List<String>>() {
                @Override
                public List<String> handle(ResultSet resultSet) throws SQLException {
                    customers = new LinkedList<>();
                    while(resultSet.next()) {
                        customers.add(resultSet.getString(1));
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            customers = new LinkedList<>();
        }
    }
    public List<String> getAllCustomers() {
        if(customers == null)
            updateCustomers();
        return customers;
    }


    public void setFuckenCustomer(String customer) {
        try {
            List<BigInteger> list = jdbcTemplate.executeSelect(GET_CUSTOMER_ID_QUERY,
                    new Object[][] {{ Types.VARCHAR, customer }}, new ResultSetHandler<List<BigInteger>>() {
                @Override
                public List<BigInteger> handle(ResultSet resultSet) throws SQLException {
                    resultSet.next();
                    List list = new ArrayList<BigInteger>();
                    list.add(resultSet.getBigDecimal(1).toBigInteger());
                    return list;
                }
            });
            trf.setCustomerId(list.get(0));
        } catch (SQLException e) {}
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private void closeDialog(Object object) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.closeDialog(object);
    }

    private void createTrf() {
        trf = objectFactory.createObject(TRF.class);
        trf.setStatusId(new BigInteger(ENTERING_STATUS_ID), user.getId());
    }

    private void loadTrf(BigInteger id) {
        trf = objectFactory.createObject(id, TRF.class);
        if("Travel Support Department".equals(user.getRole().getName()))
            trf.setStatusId(new BigInteger(COMPLETED_STATUS_ID), user.getId());
        else
            trf.setStatusId(new BigInteger(READY_STATUS_ID), user.getId());
    }

    private void setParameters() {
        if("Travel Support Department".equals(currentUserBean.getMajorRole().getName()))
            employee = trf.getEmployee();
        else
            employee = objectFactory.createObject(employees.get(employeeName), Employee.class);
        trf.setEmployeeId(employee.getId());
        trf.setHotel(hotel);
        setCity(destCity);
        setFuckenCustomer(customer);
        trf.setCarRent(carRent);
        trf.setPayCash(payCash);
        trf.setProjectManagerId(employee.getManagerId());
        trf.setDepartureDate(new Timestamp(departureDate.getTime()));
        trf.setReturnDate(new Timestamp(returnDate.getTime()));
        trf.setName(employee.getName() + " " + employee.getLastName() + " " + (customer == null ? "" : customer) + departureDate);
    }

    public void setCurrentUserBean(CurrentUserBean currentUserBean) {
        this.currentUserBean = currentUserBean;
    }

    public String getEmployee() {
        return employeeName;
    }

    public void setEmployee(String employeeName) {
        this.employeeName = employeeName;
    }

    private void updateEmployees() {
        try {
            employee = currentUserBean.getUser().getEmployee();
            BigInteger id = employee.getId();
            jdbcTemplate.executeSelect(GET_EMPLOYEES_FOR_CURRENT_USER,
                    new Object[][] {
                            {Types.BIGINT, employee.getId()},
                            {Types.BIGINT, EMPLOYEE_OBJECT_TYPE_ID}
                    }, new ResultSetHandler<List<String>>() {
                @Override
                public List<String> handle(ResultSet resultSet) throws SQLException {
                    employees = new HashMap<>();
                    while(resultSet.next()) {
                        BigInteger value = resultSet.getBigDecimal(1).toBigInteger();
                        String key = resultSet.getString(2);
                        employees.put(key, value);
                    }
                    return null;
                }
            });
        employees.put(employee.getName(), employee.getId());
        } catch (SQLException|NullPointerException en) {
            employees = new HashMap<>();
        }
    }

    public Collection<String> getEmployees() {
        if(employees == null)
            updateEmployees();
        return employees.keySet();
    }

    public boolean isNotTravelSupport() {
        if("Travel Support Department".equals(currentUserBean.getMajorRole().getName()))
            return false;
        return true;
    }

}
