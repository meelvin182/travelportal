package ru.ipccenter.travelportal.common.model;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by Anastasia on 17.03.2015.
 */
public interface TPListAttribute <T> {
    BigInteger getId();
    void setId(BigInteger id);
    T getValue();
    BigInteger getListValueTypeId();
    List<? extends TPListAttribute> getAllValues();
}
