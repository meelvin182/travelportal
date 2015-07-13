package ru.ipccenter.travelportal.ejb.stateful;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.DataAccessException;
import ru.ipccenter.travelportal.common.model.attributes.Status;
import ru.ipccenter.travelportal.common.model.objects.StatusHistoryEntry;
import ru.ipccenter.travelportal.common.model.objects.TRF;
import ru.ipccenter.travelportal.common.model.objects.User;
import ru.ipccenter.travelportal.interceptor.binding.SetOnce;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;

import javax.ejb.*;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

/**
 * Created by Anastasia on 16.03.2015.
 */
@Stateful
@Local(StatusHistoryEntry.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class StatusHistoryEntryBean extends AbstractTPObject implements StatusHistoryEntry {
    private static final Logger LOG = Logger.getLogger(StatusHistoryEntryBean.class);

    @EJB
    private JdbcTemplate jdbcTemplate;

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
    public TRF getTrf() {
        try {
            return jdbcTemplate.executeSelect(
                    "select object_id from TP_PARAMS where attr_id = ? and reference = ?",
                    new Object[][] {
                            {Types.NUMERIC, STATUS_HISTORY_ATTRIBUTE},
                            {Types.NUMERIC, getId()}
                    },
                    new ResultSetHandler<TRF>() {
                        @Override
                        public TRF handle(ResultSet resultSet) throws SQLException {
                            if (resultSet.next()) {
                                BigInteger trfId = resultSet.getBigDecimal(1).toBigInteger();
                                return objectFactory.createObject(trfId, TRF.class);
                            } else {
                                return null;
                            }
                        }
                    }
            );
        } catch (SQLException e) {
            LOG.error(e);
            throw new DataAccessException("Can't get employee", e);
        }
    }

    @Override
    public Status getStatus() {
        return listAttributeFactory.createListAttribute(getStatusId(), Status.class);
    }

    public BigInteger getStatusId() {
        return cacheProvider.provide().getParameter(getId(), ATTR_ID_STATUS).getListValueId();
    }

    public void setStatusId(BigInteger statusId) {
        cacheProvider.provide().getParameter(getId(), ATTR_ID_STATUS).setListValueId(statusId);
    }

    @Override
    public User getUser() {
        return objectFactory.createObject(getUserId(), User.class);
    }

    @Override
    public BigInteger getUserId() {
        return cacheProvider.provide().getParameter(getId(), ATTR_ID_USER).getReference();
    }

    @Override
    public void setUserId(BigInteger userId) {
        cacheProvider.provide().getParameter(getId(), ATTR_ID_USER).setReference(userId);
    }

    public Timestamp getChangeTime() {
        return cacheProvider.provide().getParameter(getId(), ATTR_ID_CHANGE_TIME).getDateValue();
    }

    public void setChangeTime(Timestamp changeTime) {
        cacheProvider.provide().getParameter(getId(), ATTR_ID_CHANGE_TIME).setDateValue(changeTime);
    }

    public String getComment() {
        return cacheProvider.provide().getParameter(getId(), ATTR_ID_COMMENT).getValue();
    }
    public void setComment(String comment) {
        cacheProvider.provide().getParameter(getId(), ATTR_ID_COMMENT).setValue(comment);
    }

    public String toString() {
        return "At " + getChangeTime() + " set " + getStatus().toString();
    }
}
