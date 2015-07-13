package ru.ipccenter.travelportal.services;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.bridge.utils.ObjectTypeExtractor;
import ru.ipccenter.travelportal.common.model.objects.City;
import ru.ipccenter.travelportal.common.model.objects.Customer;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by meelvin182 on 18.05.15.
 */
@ApplicationScoped
public class CustomerService {

    private static final Logger log = Logger.getLogger(CustomerService.class);


    public static final String CUSTOMERS_QUERY = "select object_id from tp_objects where object_type_id = ? order by order_num, name ";

    private static final BigInteger CUSTOMER_OT = ObjectTypeExtractor.extractObjectTypeId(Customer.class);
    @EJB
    private TPObjectFactory objectFactory;
    @EJB
    JdbcTemplate jdbcTemplate;



    public List<Customer> getAllCustomers() {
        try {
            return jdbcTemplate.executeSelect(
                    CUSTOMERS_QUERY,
                    new Object[][]{
                            {Types.NUMERIC, CUSTOMER_OT}
                    },
                    new ResultSetHandler<List<Customer>>() {
                        @Override
                        public List<Customer> handle(ResultSet resultSet) throws SQLException {
                            List<Customer> customers = new LinkedList<Customer>();
                            while (resultSet.next()) {
                                customers.add(objectFactory.createObject(resultSet.getBigDecimal(1).toBigInteger(), Customer.class));
                            }
                            return customers;
                        }
                    }
            );
        } catch (SQLException e) {
            log.error("", e);
            return Collections.emptyList();
        }
    }

    public Customer create() {
        return objectFactory.createObject(Customer.class);
    }

    public Customer getById(BigInteger id) {
        return objectFactory.createObject(id, Customer.class);
    }



}
