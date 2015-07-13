package ru.ipccenter.travelportal.dialogs;

import ru.ipccenter.travelportal.common.model.objects.Customer;
import ru.ipccenter.travelportal.services.CustomerService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by meelvin182 on 20.05.15.
 */

@RequestScoped
@ManagedBean
public class CustomerListBean {

    @Inject
    private CustomerService customerService;

    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteCustomer(BigInteger id) {
        Customer customer = customerService.getById(id);
        customer.delete();
    }

}
