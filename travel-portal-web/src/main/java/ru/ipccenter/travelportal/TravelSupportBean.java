package ru.ipccenter.travelportal;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import ru.ipccenter.travelportal.common.model.objects.Employee;
import ru.ipccenter.travelportal.data.holders.Entry;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;
import ru.ipccenter.travelportal.services.TRFService;
import ru.ipccenter.travelportal.session.CurrentUserBean;
import ru.ipccenter.travelportal.utils.MessageUtils;

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
import java.sql.Types;
import java.util.*;

@ViewScoped
@ManagedBean(name = "TravelSupport")
public class TravelSupportBean {
    private static final Logger log = Logger.getLogger(TravelSupportBean.class);

    private static final String GET_TRF_QUERY = "select trfs.object_id" +
            "  from tp_objects trfs, tp_params trfs_params" +
            "  where trfs.object_type_id = 9223372036854775769 AND trfs.object_id = trfs_params.object_id AND" +
            "        trfs_params.attr_id = 9223372036854775800 AND trfs_params.reference in" +
            "(select employees.object_id" +
            "  from tp_objects employees, tp_params employees_params" +
            "  where employees.object_type_id = 9223372036854775798 AND employees.object_id = employees_params.object_id AND employees_params.reference in" +
            "(select offices.object_id" +
            "  from tp_objects offices, tp_params offices_params" +
            "  where offices.object_type_id = 9223372036854775783 AND offices.object_id = offices_params.object_id and offices_params.reference in" +
            "(select cities.object_id city_id" +
            "  from tp_objects cities, tp_params cities_params" +
            "  where cities.object_type_id = 9223372036854775780 AND cities.object_id = cities_params.object_id and cities_params.reference = ? )))";
    @Inject
    CurrentUserBean currentUser;
    @Inject
    TRFService trfService;
    @EJB
    JdbcTemplate jdbcTemplate;

    List<Entry> inProgressTRFs;
    List<Entry> allTRFs;


    public void addMessage(SelectEvent event) {
        String summary = (String) event.getObject();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }


    public List<Entry> getInProgressTRFs() {
        if (inProgressTRFs != null) {
            return inProgressTRFs;
        } else {
            return inProgressTRFs = updateTRFList(true);
        }
    }


    public List<Entry> getAllTRFs() {
        if (allTRFs != null) {
            return allTRFs;
        } else {
            return allTRFs = updateTRFList(false);
        }
    }

    public CurrentUserBean getUser() {
        return currentUser;
    }

    private List<Entry> updateTRFList(final boolean inProgress) {
        Employee employee = currentUser.getUser().getEmployee();

        if (employee == null || employee.getOffice() == null || employee.getOffice().getCity() == null ||
                employee.getOffice().getCity().getCountryId() == null) {
            MessageUtils.sendMessage(null, FacesMessage.SEVERITY_WARN, "Cannot get country", "");
            return Collections.emptyList();
        }

        BigInteger countryId = employee.getOffice().getCity().getCountryId();
        try {
            return jdbcTemplate.executeSelect(GET_TRF_QUERY, new Object[][]{{Types.BIGINT, countryId}},
                    new ResultSetHandler<List<Entry>>() {
                        @Override
                        public List<Entry> handle(ResultSet resultSet) throws SQLException {
                            List<Entry> list = new LinkedList<Entry>();
                            while (resultSet.next()) {
                                Entry entry = trfService.createEntry(resultSet.getBigDecimal(1).toBigInteger());
                                if (!inProgress) {
                                    list.add(entry);
                                } else if (inProgress && ("Ready".equals(entry.getStatus()))) {
                                    list.add(entry);
                                }
                            }
                            return list;
                        }
                    });
        } catch (SQLException e) {
            log.error("Can't retrieve trf list", e);
            return Collections.emptyList();
        }
    }

    private ExternalContext prepareTRF() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest)context.getRequest();
        HttpSession session = request.getSession();
        Object attribute = session.getAttribute("trfId");
        if (attribute != null)
            session.removeAttribute("trfId");
        return context;
    }

    private Map<String, Object> getOptions() {
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 400);
        return options;
    }


    public void editTRF(String trfId) {
        ExternalContext context = prepareTRF();
        context.getSessionMap().put("trfId", trfId);
        RequestContext.getCurrentInstance().openDialog("dialogs/manage_trf", getOptions(), null);
    }

}
