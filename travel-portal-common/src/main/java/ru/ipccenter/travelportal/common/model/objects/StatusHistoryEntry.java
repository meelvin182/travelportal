package ru.ipccenter.travelportal.common.model.objects;

import ru.ipccenter.travelportal.common.model.TPObject;
import ru.ipccenter.travelportal.common.model.attributes.Status;
import ru.ipccenter.travelportal.common.model.bridge.annotations.Attribute;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ObjectType;
import ru.ipccenter.travelportal.common.model.bridge.utils.AttributeExtractor;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * Created by Ivan on 12.03.2015.
 */
@ObjectType(id = "9223372036854775771",
    name = "status_history",
    attributes = {
        @Attribute(id = "9223372036854775770", name = "Status"),
        @Attribute(id = "9223372036854775791", name = "User"),
        @Attribute(id = "9223372036854775754", name = "Change Time"),
        @Attribute(id = "9223372036854775753", name = "Comment"),
})
public interface StatusHistoryEntry extends TPObject, Serializable {
    static final BigInteger STATUS_HISTORY_ATTRIBUTE = TRF.STATUS_HISTORY_ATTRIBUTE;
    static final BigInteger ATTR_ID_STATUS = AttributeExtractor
            .extractAttributeId(StatusHistoryEntry.class, "Status");
    static final BigInteger ATTR_ID_USER = AttributeExtractor
            .extractAttributeId(StatusHistoryEntry.class, "User");
    static final BigInteger ATTR_ID_CHANGE_TIME = AttributeExtractor
            .extractAttributeId(StatusHistoryEntry.class, "Change Time");
    static final BigInteger ATTR_ID_COMMENT = AttributeExtractor
            .extractAttributeId(StatusHistoryEntry.class, "Comment");

    TRF getTrf();

    Status getStatus();
    void setStatusId(BigInteger statusId);

    public BigInteger getUserId();
    public void setUserId(BigInteger userId);

    public User getUser();

    Timestamp getChangeTime();
    void setChangeTime(Timestamp changeTime);

    String getComment();
    void setComment(String comment);
}
