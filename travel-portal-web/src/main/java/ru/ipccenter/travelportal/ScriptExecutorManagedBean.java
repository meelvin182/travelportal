package ru.ipccenter.travelportal;

import org.apache.log4j.Logger;
import org.jboss.security.SecurityUtil;
import org.jboss.security.auth.spi.Util;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.security.auth.Subject;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@ManagedBean(name = "ScriptExecuter")
@SessionScoped
public class ScriptExecutorManagedBean implements Serializable{
    private static final String SUBJECT_CONTEXT_KEY = "javax.security.auth.Subject.container";

    public static final Logger LOG = Logger.getLogger(ScriptExecutorManagedBean.class);


    @EJB
    private JdbcTemplate jdbcTemplate;

    Subject subject;

    private String query = null;
    SQLException exception;
    private List<String> list = null;
    private int rowsUpdated;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String DML() {
        try {
            rowsUpdated = jdbcTemplate.executeUpdate(query);
        } catch (SQLException e) {
            exception = e;
            return "script_executer.xhtml";
        }
        return "";
    }

    public String submit() {
        try {
        list = jdbcTemplate.executeSelect(query, new ResultSetHandler<List<String>>() {
            @Override
            public List<String> handle(ResultSet resultSet) throws SQLException {
                List<String> list = new LinkedList<>();
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

                while (resultSet.next()) {
                    for (int columnIndex = 1; columnIndex <= resultSetMetaData.getColumnCount(); columnIndex++)
                        list.add(resultSet.getString(columnIndex));
                }

                return list;
            }
        });
        } catch (SQLException e) {
            exception = e;
            return "script_executer.xhtml";
        }
        return "";
    }

    public String revert() {
        query = null;
        return "";
    }

    public String getList() {
        if(list == null)
            return null;
        StringBuilder stringBuilder = new StringBuilder("result: ");
            for(int i = 0; i < list.size(); i++) {
                stringBuilder.append(list.get(i)).append(",                   ");
                if((i + 1) % 6 == 0)
                    stringBuilder.append("\n");
            }
            stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    public String getException() {
        StringBuilder stringBuilder = new StringBuilder();
        for(StackTraceElement stackTraceElement: exception.getStackTrace()) {
            stringBuilder.append(stackTraceElement.toString());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public String getRole() {
        try {
            subject = (Subject) PolicyContext.getContext(SUBJECT_CONTEXT_KEY);
        } catch (PolicyContextException e) {
            return "error мать вашу";
        }
        if(subject == null)
            return "нету)";
        return String.valueOf(SecurityUtil.getSubjectRoles(subject));
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(false);
        if(session != null)
            session.invalidate();
        context.redirect("/index.xhtml");
    }

    public String getHash() {
        String passwordAndSalt = "travelportalcommdep_user_salt";
        return Util.createPasswordHash("MD5", Util.BASE64_ENCODING, null, null, passwordAndSalt);
    }

}
