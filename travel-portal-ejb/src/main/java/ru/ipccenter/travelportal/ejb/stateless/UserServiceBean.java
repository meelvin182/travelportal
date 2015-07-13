package ru.ipccenter.travelportal.ejb.stateless;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.caches.LRUCache;
import ru.ipccenter.travelportal.common.caches.LoadCallback;
import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ObjectType;
import ru.ipccenter.travelportal.common.model.objects.Role;
import ru.ipccenter.travelportal.common.model.objects.User;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;
import ru.ipccenter.travelportal.security.UserService;

import javax.ejb.*;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Local(UserService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserServiceBean implements UserService{
    public static final Logger LOG = Logger.getLogger(UserService.class);

    private static final String USER_OT_ID = User.class.getAnnotation(ObjectType.class).id();

    @EJB
    JdbcTemplate jdbcTemplate;

    private class UserIdCache extends LRUCache<String, BigInteger> {

        public UserIdCache() {
            super(new LoadCallback<String, BigInteger>() {
                @Override
                public BigInteger load(String key) {

                    try {
                        return jdbcTemplate.executeSelect(
                                "select object_id from tp_objects where name = ? and object_type_id = ?",
                                new Object[][]{
                                        {Types.VARCHAR, key},
                                        {Types.VARCHAR, USER_OT_ID}
                                },
                                new ResultSetHandler<BigInteger>() {
                                    public BigInteger handle(ResultSet resultSet) throws SQLException {
                                        if (resultSet.next()) {
                                            return resultSet.getBigDecimal(1).toBigInteger();
                                        } else {
                                            return BigInteger.ZERO;
                                        }
                                    }
                                });
                    } catch (SQLException e) {
                        LOG.error(e);
                        return null;
                    }
                }
            });
        }

        @Override
        protected Logger getLogger() {
            return LOG;
        }

        @Override
        protected Integer getDefaultSize() {
            return 16;
        }

        @Override
        protected Integer getSize() throws Exception {
            return 16;
        }
    }


    private UserIdCache cache = new UserIdCache();

    @EJB
    TPObjectFactory objectFactory;

    @Override
    public BigInteger getUserId(String userName){
        return cache.get(userName);
    }

    @Override
    public User getUser(String userName) {
        return objectFactory.createObject(getUserId(userName), User.class);
    }

    @Override
    public String getPasswordHash(String userName) {
        return getUser(userName).getPassword();
    }

    @Override
    public String getSalt(String userName) {
        return getUser(userName).getSalt();
    }

    @Override
    public Role getRole(String userName) {
        return getUser(userName).getRole();
    }
}
