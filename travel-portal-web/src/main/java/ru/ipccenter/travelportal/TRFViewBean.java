package ru.ipccenter.travelportal;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.DataAccessException;
import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.objects.*;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ObjectType;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;


/**
* Created by user on 05.04.2015.
*/
@ViewScoped
@ManagedBean(name = "trfViewBean")
public class TRFViewBean {
    private static final BigInteger EMPLOYEE_TYPE_ID = new BigInteger(
            Employee.class.getAnnotation(ObjectType.class).id());
    private static final BigInteger CUSTOMER_TYPE_ID = new BigInteger(
            Customer.class.getAnnotation(ObjectType.class).id());
    private static final BigInteger CITY_TYPE_ID = new BigInteger(
            City.class.getAnnotation(ObjectType.class).id());
    private static final Logger LOG = Logger.getLogger(TRFViewBean.class);

    private TRF trf;

    private List<Employee> employees;
    private List<Customer> customers;
    private List<City> cities;

    @EJB
    private TPObjectFactory objectFactory;
    @EJB
    private JdbcTemplate jdbcTemplate;

    public void createTrf() {
        trf = objectFactory.createObject(TRF.class);
    }

    public void loadTrf(BigInteger id) {
        trf = objectFactory.createObject(id, TRF.class);
    }

    public TRF getTrf() {
        return trf;
    }

    public void setTrf(TRF trf) {
        this.trf = trf;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    @PostConstruct
    public void init() throws Exception {
//        trf = objectFactory.createObject(TRF.class);
        initEmployees();
        initCustomers();
        initCities();
    }

    private void initEmployees() {
        try {
            employees = jdbcTemplate.executeSelect(
                    "select object_id from tp_objects where object_type_id = ?",
                    new Object[][]{
                            {Types.NUMERIC, EMPLOYEE_TYPE_ID}
                    },
                    new ResultSetHandler<List<Employee>>() {
                        @Override
                        public List<Employee> handle(ResultSet resultSet) throws SQLException {
                            List<Employee> list = new LinkedList<>();

                            while (resultSet.next()) {
                                BigInteger employee_id = new BigInteger(resultSet.getNString("object_id"));
                                list.add(objectFactory.createObject(employee_id, Employee.class));
                            }
                            return list;
                        }
                    }
            );
        } catch (SQLException e) {
            LOG.error(e);
            throw new DataAccessException("Can't get employees", e);
        }
    }

    private void initCustomers() {
        try {
            customers = jdbcTemplate.executeSelect(
                    "select object_id from tp_objects where object_type_id = ?",
                    new Object[][]{
                            {Types.NUMERIC, CUSTOMER_TYPE_ID}
                    },
                    new ResultSetHandler<List<Customer>>() {
                        @Override
                        public List<Customer> handle(ResultSet resultSet) throws SQLException {
                            List<Customer> list = new LinkedList<>();

                            while (resultSet.next()) {
                                BigInteger customer_id = new BigInteger(resultSet.getNString("object_id"));
                                list.add(objectFactory.createObject(customer_id, Customer.class));
                            }
                            return list;
                        }
                    }
            );
        } catch (SQLException e) {
            LOG.error(e);
            throw new DataAccessException("Can't get countries", e);
        }
    }

    private void initCities() {
        try {
            cities = jdbcTemplate.executeSelect(
                    "select object_id from tp_objects where object_type_id = ?",
                    new Object[][]{
                            {Types.NUMERIC, CITY_TYPE_ID}
                    },
                    new ResultSetHandler<List<City>>() {
                        @Override
                        public List<City> handle(ResultSet resultSet) throws SQLException {
                            List<City> list = new LinkedList<>();

                            while (resultSet.next()) {
                                BigInteger city_id = new BigInteger(resultSet.getNString("object_id"));
                                list.add(objectFactory.createObject(city_id, City.class));
                            }
                            return list;
                        }
                    }
            );
        } catch (SQLException e) {
            LOG.error(e);
            throw new DataAccessException("Can't get cities", e);
        }
    }

}
