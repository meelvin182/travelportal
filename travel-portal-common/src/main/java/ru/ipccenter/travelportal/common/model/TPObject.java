package ru.ipccenter.travelportal.common.model;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by Ivan on 12.03.2015.
 */
public interface TPObject {
    BigInteger getId();
    void setId(BigInteger id);

    BigInteger getParentId();
    void setParentId(BigInteger id);
    List<BigInteger> getChildIds();

    String getName();
    void setName(String name);

    void delete();
    void unused();
}
