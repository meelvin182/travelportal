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
@ManagedBean(name = "Employee")
public class EmployeeBean {
    private static final Logger log = Logger.getLogger(EmployeeBean.class);

    private static final String GET_TRF_QUERY = "select object_id from tp_params " +
            "where reference = ? " +
            "and object_id in (select object_id from tp_objects where object_type_id = 9223372036854775769)";
    @Inject
    CurrentUserBean currentUser;
    @Inject
    TRFService trfService;
    @EJB
    JdbcTemplate jdbcTemplate;

    List<Entry> inProgressTRFs;
    List<Entry> allTRFs;


    public void createNewTRF() {
        prepareTRF();
        RequestContext.getCurrentInstance().openDialog("dialogs/manage_trf", getOptions(), null);
    }


    public void addMessage(SelectEvent event) {
        String summary = (String) event.getObject();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }


    public List<Entry> getInProgressTRFs() {
        if (inProgressTRFs == null)
            updateTRFList();
        return inProgressTRFs;
    }


    public List<Entry> getAllTRFs() {
        if(allTRFs == null)
            updateTRFList();
        return allTRFs;
    }

    private void updateTRFList() {
        allTRFs = new LinkedList<>();
        inProgressTRFs = new LinkedList<>();
        Employee employee = currentUser.getUser().getEmployee();

        if (employee == null) {
            return;
        }
        try {
            jdbcTemplate.executeSelect(GET_TRF_QUERY, new Object[][]{{Types.BIGINT, employee.getId()}},
                    new ResultSetHandler<List<Entry>>() {
                        @Override
                        public List<Entry> handle(ResultSet resultSet) throws SQLException {
                            while(resultSet.next()) {
                                Entry entry = trfService.createEntry(resultSet.getBigDecimal(1).toBigInteger());
                                allTRFs.add(entry);
                                if (("Entering".equals(entry.getStatus()) || "Rejected".equals(entry.getStatus()))) {
                                    inProgressTRFs.add(entry);
                                }
                            }
                            return null;
                        }
                    });
        } catch (SQLException e) {
            log.error("Can't retrieve trf list", e);
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

    public void addEntry(BigInteger trfId) {
        Entry entry = trfService.createEntry(trfId);
        inProgressTRFs.add(entry);
        allTRFs.add(entry);
//        FacesContext ctxt = FacesContext.getCurrentInstance();
//        ctxt.getPartialViewContext().getRenderIds().add("editbl");
//        RequestContext.getCurrentInstance().update("editbl");
    }

}




