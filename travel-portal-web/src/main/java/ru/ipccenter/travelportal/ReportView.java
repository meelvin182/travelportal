package ru.ipccenter.travelportal;


import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ObjectType;
import ru.ipccenter.travelportal.common.model.bridge.utils.AttributeExtractor;
import ru.ipccenter.travelportal.common.model.objects.Department;
import ru.ipccenter.travelportal.common.model.objects.Employee;
import ru.ipccenter.travelportal.common.model.objects.Office;
import ru.ipccenter.travelportal.common.model.objects.TRF;
import ru.ipccenter.travelportal.data.holders.Entry;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;
import ru.ipccenter.travelportal.services.DepartmentService;
import ru.ipccenter.travelportal.services.TRFService;
import ru.ipccenter.travelportal.session.CurrentUserBean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.*;

@SessionScoped
@ManagedBean(name = "ReportView")
public class ReportView {
    public static final Logger LOG = Logger.getLogger(ReportView.class);

    static final String GET_OFFICES_QUERY =
            "select object_id, name\n" +
            "from tp_objects\n" +
            "where object_type_id = ?"; /* office */

    static final String IN_TRIP_TRFS_QUERY =
            "select object_id from tp_params\n" +
            "where attr_id = ?\n" + /* departure date */
            "and date_value < ?\n" +
            "and object_id in" +
            "(" +
            "select object_id\n" +
            "from tp_params\n" +
            "where attr_id = ?\n" + /* return date */
            "and date_value > ?\n" +
            ")";

    static final String PLANNED_TRIP_TRFS_QUERY =
            "select object_id\n" +
            "from tp_params\n" +
            "where attr_id = ?\n" + /* departure date */
            "and date_value > ?";

    static final BigInteger DEPARTURE_DATE_ATTRIBUTE = AttributeExtractor
            .extractAttributeId(TRF.class, "Departure Date");
    static final BigInteger RETURN_DATE_ATTRIBUTE     = AttributeExtractor
            .extractAttributeId(TRF.class, "Return Date");
    static final BigInteger OFFICE_OBJECT_TYPE_ID = new BigInteger(
            Office.class.getAnnotation(ObjectType.class).id());

    Map<String, BigInteger> offices;
    List<Entry> inTrip;
    List<Entry> plannedTrips;

    @EJB
    JdbcTemplate jdbcTemplate;

    @Inject
    TRFService trfService;

    private String department;
    private String office;

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Inject
    DepartmentService departmentService;

    @Inject
    CurrentUserBean currentUserBean;

    private void updateOffices() {
        try {
            jdbcTemplate.executeSelect(GET_OFFICES_QUERY,
                    new Object[][] {
                            {Types.BIGINT, OFFICE_OBJECT_TYPE_ID}
                    }, new ResultSetHandler<Collection<String>>() {
                @Override
                public Collection<String> handle(ResultSet resultSet) throws SQLException {
                    offices = new HashMap<>();
                    while(resultSet.next()) {
                        BigInteger value = resultSet.getBigDecimal(1).toBigInteger();
                        String key = resultSet.getString(2);
                        while (offices.containsKey(key)) {
                            int i = 2;
                            key += " $" + i;
                            i++;
                        }
                        offices.put(key, value);
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            LOG.error(e);
            offices =  new HashMap<>();
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    private void updateInTrip() {
        Timestamp today = new Timestamp(new Date().getTime());
        try {
            jdbcTemplate.executeSelect(IN_TRIP_TRFS_QUERY,
                    new Object[][] {
                            {Types.BIGINT, DEPARTURE_DATE_ATTRIBUTE},
                            {Types.TIMESTAMP, today},
                            {Types.BIGINT, RETURN_DATE_ATTRIBUTE},
                            {Types.TIMESTAMP, today}
                    }, new ResultSetHandler<List<Entry>>() {
                @Override
                public List<Entry> handle(ResultSet resultSet) throws SQLException {
                    inTrip = new LinkedList<>();
                    while (resultSet.next()) {
                        BigInteger trfId = resultSet.getBigDecimal(1).toBigInteger();
                        inTrip.add(trfService.createEntryWithOffice(trfId));
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            LOG.error(e);
            inTrip = new LinkedList<>();
        }
    }

    public List<Department> getDepartments() {
        return departmentService.getMajorDepartments();
    }

    public Collection<String> getOffices() {
        if(offices == null)
            updateOffices();
        return offices.keySet();
    }

    public List<Entry> inTrip() {
        if(inTrip == null)
            updateInTrip();
        filter();
        return inTrip;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    private void updatePlannedTrips(){
        Timestamp today = new Timestamp(new Date().getTime());
        try {
            jdbcTemplate.executeSelect(PLANNED_TRIP_TRFS_QUERY,
                    new Object[][] {
                            {Types.BIGINT, DEPARTURE_DATE_ATTRIBUTE},
                            {Types.TIMESTAMP, today}
                    }, new ResultSetHandler<List<Entry>>() {
                @Override
                public List<Entry> handle(ResultSet resultSet) throws SQLException {
                    plannedTrips = new LinkedList<>();
                    while (resultSet.next()) {
                        BigInteger trfId = resultSet.getBigDecimal(1).toBigInteger();
                        plannedTrips.add(trfService.createEntryWithOffice(trfId));
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            LOG.error(e);
            plannedTrips = new LinkedList<>();
        }
    }

    public List<Entry> plannedTrips() {
        if(plannedTrips == null)
            updatePlannedTrips();
        return plannedTrips;
    }

    private void filter() {
        if(office == null) {
            try {
                office = currentUserBean.getUser().getEmployee().getOffice().getName();
            } catch (NullPointerException e) {
                LOG.error(e);
                office = "";
            }
        }
        List<Entry> temp = new LinkedList<>();
        for(Entry entry: inTrip) {
            String officeName = entry.getOffice();
            int dollarIndex = office.indexOf('$');
            if(dollarIndex >= 0)
                office = office.substring(0, dollarIndex - 1);
            if(officeName.equals(office))
                temp.add(entry);
        }
        inTrip = temp;

    }
    public void submit() {
        filter();
    }

    public String download() {
        String fileName = "report.xls";
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        ec.responseReset();
        ec.setResponseContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.


        try {
            OutputStream output = ec.getResponseOutputStream();
            output.write("vgbhnj sfdbjbkjbb,h ,sdhfv,jshdf jsdgjk sdkgj skdjgbksjb k,. . eg.e e. gweg ew gw\nsdgsdfgsd \n sdfbsdf bdf\n ssgdf sdf".getBytes());
            output.close();
        } catch (IOException e) {
            LOG.error(e);
        } finally {

            fc.responseComplete();
            return ".";
        }
    }
}
