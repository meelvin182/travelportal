package ru.ipccenter.travelportal.dialogs;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import ru.ipccenter.travelportal.common.model.objects.City;
import ru.ipccenter.travelportal.common.model.objects.Customer;
import ru.ipccenter.travelportal.services.CustomerService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import ru.ipccenter.travelportal.services.CustomerService;

/**
 * Created by meelvin182 on 18.05.15.
 */
@ManagedBean
@ViewScoped
public class CustomerBean {
   // private static final Logger log = Logger.getLogger(CustomerBean.class);

    public static final String CUSTOMER_ID_KEY = "customer-id";

    @Inject
    private CustomerService customerService;

    private BigInteger customerId;

    private String name;


    public String getId() {
        return (customerId != null) ? customerId.toString(): null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String customerId) {
       //TODO:sdelat' vse chetko
    }
    @Transactional(Transactional.TxType.REQUIRED)
    public void save(){
        Customer customer = (customerId!= null)
                ? customerService.getById(customerId)
                : customerService.create();

        customer.setName(name.trim());

        RequestContext.getCurrentInstance().closeDialog(null);
    }

}
