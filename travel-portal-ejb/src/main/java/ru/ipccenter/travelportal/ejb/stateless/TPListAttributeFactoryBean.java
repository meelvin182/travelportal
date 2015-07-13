package ru.ipccenter.travelportal.ejb.stateless;

import ru.ipccenter.travelportal.common.model.TPListAttribute;
import ru.ipccenter.travelportal.common.model.TPListAttributeFactory;
import ru.ipccenter.travelportal.common.model.TPListAttributeNotFoundException;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ListValueType;
import ru.ipccenter.travelportal.common.utils.BeanLookupHelper;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;
import ru.ipccenter.travelportal.metamodel.Dao;
import ru.ipccenter.travelportal.metamodel.entities.MMListValue;

import javax.ejb.*;
import javax.naming.NamingException;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Local(TPListAttributeFactory.class)
public class TPListAttributeFactoryBean implements TPListAttributeFactory {
    @EJB
    private Dao dao;
    @EJB
    JdbcTemplate jdbcTemplate;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <O extends TPListAttribute> O createListAttribute(BigInteger id, Class<O> clazz) {
        if (dao.getById(id, MMListValue.class) == null) {
            throw new TPListAttributeNotFoundException("MMListValue with id #" + id + " doesn't exist");
        } else {
            try {
                O listAttribute = BeanLookupHelper.lookup(clazz);
                listAttribute.setId(id);
                return listAttribute;
            } catch (NamingException e) {
                throw new TPListAttributeNotFoundException("Couldn't build TPListAttribute" + e);
            }
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <O extends TPListAttribute> List<O> createAllListAttributes(Class<O> clazz) {
        List<O> listAttributes = null;
        try {
            listAttributes = new ArrayList<O>();
            List<BigInteger> idList;
            idList = jdbcTemplate.executeSelect("SELECT id FROM list_values WHERE list_value_type_id = ?", new Object[][]{
                            {Types.BIGINT, clazz.getAnnotation(ListValueType.class).id()}
                    },
                    new ResultSetHandler <List<BigInteger>>() {
                        @Override
                        public List<BigInteger> handle(ResultSet resultSet) throws SQLException {
                            List<BigInteger> idList = new ArrayList<BigInteger>();
                            while (resultSet.next()) {
                                BigInteger current = resultSet.getBigDecimal("id").toBigInteger();
                                idList.add(current);
                            }
                            return idList;
                        }
                    });
            for (BigInteger id: idList) {
                listAttributes.add(createListAttribute(id, clazz));
            }
        } catch (SQLException e) {
            throw new TPListAttributeNotFoundException("Couldn't build list of TPListAttribute", e);
        }
        return listAttributes;
    }
}
