package ru.ipccenter.travelportal.services;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.bridge.utils.ObjectTypeExtractor;
import ru.ipccenter.travelportal.common.model.objects.Role;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ivan on 18.05.2015.
 */
@ApplicationScoped
public class RoleService {

    private static final Logger log = Logger.getLogger(RoleService.class);
    private static final BigInteger ROLE_OT = ObjectTypeExtractor.extractObjectTypeId(Role.class);
    private static final String ROLE_QUERY =
            "select\n" +
            "  object_id\n" +
            "from\n" +
            "  tp_objects\n" +
            "where\n" +
            "  object_type_id = ?";

    @EJB
    private TPObjectFactory factory;
    @EJB
    private JdbcTemplate jdbcTemplate;

    private HashMap<BigInteger, Role> rolesById;
    private List<Role> rolesList;

    @PostConstruct
    public void init() {
        rolesById = new HashMap<>();
        rolesList = new LinkedList<>();

        try {
            jdbcTemplate.executeSelect(
                    ROLE_QUERY,
                    new Object[][]{
                            {Types.NUMERIC, ROLE_OT}
                    },
                    new ResultSetHandler<Void>() {
                        @Override
                        public Void handle(ResultSet resultSet) throws SQLException {
                            while (resultSet.next()) {
                                Role role = factory.createObject(resultSet.getBigDecimal(1).toBigInteger(), Role.class);
                                rolesById.put(role.getId(), role);
                                rolesList.add(role);
                            }
                            return null;
                        }
                    });
        } catch (SQLException e) {
            log.error("", e);
        }
    }

    public Role getById(BigInteger id) {
        return rolesById.get(id);
    }

    public List<Role> getRolesById() {
        return new ArrayList<>(rolesList);
    }
}
