package ru.ipccenter.travelportal.data.holders;

import java.math.BigInteger;

/**
 * Created by 111 on 20.04.2015.
 */
public final class CityInfo {

    BigInteger id;
    String name;

    public CityInfo() {
    }

    public CityInfo(BigInteger id, String name) {
        this.id = id;
        this.name = name;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
