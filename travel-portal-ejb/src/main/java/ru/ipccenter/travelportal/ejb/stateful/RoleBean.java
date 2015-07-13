package ru.ipccenter.travelportal.ejb.stateful;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.attributes.Tab;
import ru.ipccenter.travelportal.common.model.objects.Role;
import ru.ipccenter.travelportal.interceptor.binding.SetOnce;
import ru.ipccenter.travelportal.metamodel.entities.MMParameter;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Stateful
@Local(Role.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class RoleBean extends AbstractTPObject implements Role {
    private static final Logger LOG = Logger.getLogger(RoleBean.class);

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
    public List<Tab> getTabs() {
        List<MMParameter> tabs = cacheProvider.provide().getParameters(getId(), TABS_ATTRIBUTE);
        List<Tab> result = new ArrayList<>(tabs.size());

        for (MMParameter tab: tabs) {
            result.add(listAttributeFactory.createListAttribute(tab.getListValueId(), Tab.class));
        }

        return result;
    }
}
